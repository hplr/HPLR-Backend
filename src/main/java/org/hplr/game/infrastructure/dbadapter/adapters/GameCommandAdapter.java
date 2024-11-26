package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.vo.GameHistoricalElo;
import org.hplr.game.core.model.vo.GameSidePlayerDataSnapshot;
import org.hplr.game.core.model.vo.GameSideSnapshot;
import org.hplr.game.core.usecases.port.out.command.*;
import org.hplr.game.infrastructure.dbadapter.entities.*;

import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.mapper.LocationMapper;

import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.game.infrastructure.dbadapter.repositories.*;

import org.hplr.library.exception.HPLRIllegalStateException;

import org.hplr.location.infrastructure.dbadapter.repositories.LocationRepository;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper.mapScore;

@Slf4j
@Service
public class GameCommandAdapter implements SaveGameCommandInterface,
        StartAllDueGamesCommandInterface,
        SaveFinishedGameCommandInterface,
        StartGameCommandInterface,
        SaveAllGamesCommandInterface {

    final LocationRepository locationRepository;
    final PlayerQueryRepository playerQueryRepository;
    final GameRepository gameRepository;
    final GameMissionRepository gameMissionRepository;
    final GameDeploymentRepository gameDeploymentRepository;
    final GameArmyTypeRepository gameArmyTypeRepository;
    final GameHistoricalEloRepository gameHistoricalEloRepository;

    public GameCommandAdapter(LocationRepository locationRepository, PlayerQueryRepository playerQueryRepository, GameRepository gameRepository, GameMissionRepository gameMissionRepository, GameDeploymentRepository gameDeploymentRepository, GameArmyTypeRepository gameArmyTypeRepository,
                              GameHistoricalEloRepository gameHistoricalEloRepository) {
        this.locationRepository = locationRepository;
        this.playerQueryRepository = playerQueryRepository;
        this.gameRepository = gameRepository;
        this.gameMissionRepository = gameMissionRepository;
        this.gameDeploymentRepository = gameDeploymentRepository;
        this.gameArmyTypeRepository = gameArmyTypeRepository;
        this.gameHistoricalEloRepository = gameHistoricalEloRepository;
    }


    @Override
    public void saveGame(GameSnapshot gameSnapshot) {
        List<PlayerEntity> allPlayerEntityList = playerQueryRepository.findAll();
        if (allPlayerEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("Not enough players!");
        }
        List<GameArmyTypeEntity> armyTypeEntityList = gameArmyTypeRepository.findAll();
        if (armyTypeEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("No army types!");
        }
        LocationEntity locationEntity = null;
        if (Objects.nonNull(gameSnapshot.gameLocation().location().getLocationId())) {
            Optional<LocationEntity> locationEntityOptional = locationRepository.findByLocationId(gameSnapshot.gameLocation().location().getLocationId().locationId());
            locationEntity = locationEntityOptional.orElseGet(() -> LocationMapper.fromSnapshot(gameSnapshot.gameLocation().location().toSnapshot()));
        }
        GameMissionEntity gameMissionEntity;
        Optional<GameMissionEntity> gameMissionEntityOptional = gameMissionRepository.findByName(gameSnapshot.gameData().gameMission().name());
        gameMissionEntity = gameMissionEntityOptional.orElseGet(() -> new GameMissionEntity(
                null,
                gameSnapshot.gameData().gameMission().name()
        ));
        GameDeploymentEntity gameDeploymentEntity;
        Optional<GameDeploymentEntity> gameDeploymentEntityOptional = gameDeploymentRepository.findByName(gameSnapshot.gameData().gameDeployment().name());
        gameDeploymentEntity = gameDeploymentEntityOptional.orElseGet(() -> new GameDeploymentEntity(
                null,
                gameSnapshot.gameData().gameDeployment().name()
        ));
        GameEntity gameEntity = GameDatabaseMapper.fromSnapshot(gameSnapshot);
        gameEntity.setLocationEntity(locationEntity);
        gameEntity.setGameMissionEntity(gameMissionEntity);
        gameEntity.setGameDeploymentEntity(gameDeploymentEntity);
        gameEntity.setFirstGameSide(new GameSideEntity(
                null,
                gameSnapshot.firstGameSide().sideId().sideId(),
                gameSnapshot.firstGameSide().allegiance(),
                mapGamePlayerDataEntityList(gameSnapshot.firstGameSide(), allPlayerEntityList, armyTypeEntityList),
                gameSnapshot.firstGameSide().isFirst(),
                mapScore(gameSnapshot.firstGameSide())));
        if(Objects.nonNull(gameSnapshot.secondGameSide())){
            gameEntity.setSecondGameSide(new GameSideEntity(
                    null,
                    gameSnapshot.secondGameSide().sideId().sideId(),
                    gameSnapshot.secondGameSide().allegiance(),
                    mapGamePlayerDataEntityList(gameSnapshot.secondGameSide(), allPlayerEntityList, armyTypeEntityList),
                    gameSnapshot.secondGameSide().isFirst(),
                    mapScore(gameSnapshot.secondGameSide())));
        }
        gameRepository.save(gameEntity);
    }

    @Override
    public void startAllDueGames(List<GameSnapshot> gameToStartList) {
        List<UUID> gameToStartIdList = gameToStartList
                .stream()
                .map(game -> game.gameId().gameId())
                .toList();
        gameToStartIdList.forEach(
             id -> log.info("Games started: {}",id)
        );
        if(!gameToStartIdList.isEmpty()){
            gameRepository.startAllDueGames(gameToStartIdList);
        }
    }

    @Override
    public void saveFinishedGame(GameSnapshot gameSnapshot, GameHistoricalElo gameHistoricalElo) {
        GameEntity gameEntity = gameRepository.findByGameId(gameSnapshot.gameId().gameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        gameEntity.setStatus(gameSnapshot.gameStatus());
        gameEntity.getFirstGameSide().getGamePlayerDataEntityList().forEach(player -> updateScore(player, gameSnapshot));
        gameEntity.getSecondGameSide().getGamePlayerDataEntityList().forEach(player -> updateScore(player, gameSnapshot));

        gameRepository.save(gameEntity);
        GameHistoricalEloEntity gameHistoricalEloEntity = new GameHistoricalEloEntity(
                null,
                gameEntity,
                gameHistoricalElo.firstSideElo().ELOValue(),
                gameHistoricalElo.secondSideElo().ELOValue()
        );
        gameHistoricalEloRepository.save(gameHistoricalEloEntity);
    }

    @Override
    public void startGame(GameSnapshot gameSnapshot) {
        gameRepository.startGame(gameSnapshot.gameId().gameId());
        log.info("Game started: {}",gameSnapshot.gameId().gameId());
    }

    private void updateScore(GamePlayerDataEntity gamePlayerDataEntity, GameSnapshot gameSnapshot) {

            Optional<GameSidePlayerDataSnapshot> gameSidePlayerDataSnapshot = gameSnapshot.firstGameSide().gameSidePlayerDataList()
                    .stream()
                    .filter(playerSnapshot ->
                            gamePlayerDataEntity.getPlayerEntity().getUserId().equals(playerSnapshot.player().userId().id()))
                    .findFirst();
        gameSidePlayerDataSnapshot.ifPresent(sidePlayerDataSnapshot -> gamePlayerDataEntity.getPlayerEntity().setScore(sidePlayerDataSnapshot.player().playerRanking().score()));
    }

    @Override
    public void saveAllGamesCommandInterface(List<GameSnapshot> gameSnapshotList) {
        List<PlayerEntity> allPlayerEntityList = playerQueryRepository.findAll();
        if (allPlayerEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("Not enough players!");
        }
        List<GameArmyTypeEntity> armyTypeEntityList = gameArmyTypeRepository.findAll();
        if (armyTypeEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("No army types!");
        }
        gameSnapshotList.forEach(gameSnapshot -> {

                    LocationEntity locationEntity = null;
                    if (Objects.nonNull(gameSnapshot.gameLocation().location().getLocationId())) {
                        Optional<LocationEntity> locationEntityOptional = locationRepository.findByLocationId(gameSnapshot.gameLocation().location().getLocationId().locationId());
                        locationEntity = locationEntityOptional.orElseGet(() -> LocationMapper.fromSnapshot(gameSnapshot.gameLocation().location().toSnapshot()));
                    }
                    GameMissionEntity gameMissionEntity;
                    Optional<GameMissionEntity> gameMissionEntityOptional = gameMissionRepository.findByName(gameSnapshot.gameData().gameMission().name());
                    gameMissionEntity = gameMissionEntityOptional.orElseGet(() -> new GameMissionEntity(
                            null,
                            gameSnapshot.gameData().gameMission().name()
                    ));
                    GameDeploymentEntity gameDeploymentEntity;
                    Optional<GameDeploymentEntity> gameDeploymentEntityOptional = gameDeploymentRepository.findByName(gameSnapshot.gameData().gameDeployment().name());
                    gameDeploymentEntity = gameDeploymentEntityOptional.orElseGet(() -> new GameDeploymentEntity(
                            null,
                            gameSnapshot.gameData().gameDeployment().name()
                    ));
                    GameEntity gameEntity = GameDatabaseMapper.fromSnapshot(gameSnapshot);
                    gameEntity.setLocationEntity(locationEntity);
                    gameEntity.setGameMissionEntity(gameMissionEntity);
                    gameEntity.setGameDeploymentEntity(gameDeploymentEntity);
                    gameEntity.setFirstGameSide(new GameSideEntity(
                            null,
                            gameSnapshot.firstGameSide().sideId().sideId(),
                            gameSnapshot.firstGameSide().allegiance(),
                            mapGamePlayerDataEntityList(gameSnapshot.firstGameSide(), allPlayerEntityList, armyTypeEntityList),
                            gameSnapshot.firstGameSide().isFirst(),
                            mapScore(gameSnapshot.firstGameSide())));
                    if(Objects.nonNull(gameSnapshot.secondGameSide())){
                        gameEntity.setSecondGameSide(new GameSideEntity(
                                null,
                                gameSnapshot.secondGameSide().sideId().sideId(),
                                gameSnapshot.secondGameSide().allegiance(),
                                mapGamePlayerDataEntityList(gameSnapshot.secondGameSide(), allPlayerEntityList, armyTypeEntityList),
                                gameSnapshot.secondGameSide().isFirst(),
                                mapScore(gameSnapshot.secondGameSide())));
                    }
                    gameRepository.save(GameDatabaseMapper.fromSnapshot(gameSnapshot));
                }
                );


    }

    public static List<GamePlayerDataEntity> mapGamePlayerDataEntityList(GameSideSnapshot gameSide, List<PlayerEntity> playerEntityList, List<GameArmyTypeEntity> gameArmyTypeEntityList) {
        List<GamePlayerDataEntity> gamePlayerDataEntityList = new ArrayList<>();
        gameSide.gameSidePlayerDataList().forEach(gameSidePlayerData ->
                {
                    PlayerEntity playerEntity = playerEntityList.stream().filter(playerEntity1 -> playerEntity1.getUserId().equals(gameSidePlayerData.player().userId().id())).findFirst().orElseThrow(NoSuchElementException::new);
                    GameArmyTypeEntity primaryGameArmyTypeEntity = gameArmyTypeEntityList.stream().filter(gameArmyTypeEntity -> gameArmyTypeEntity.getName().equals(gameSidePlayerData.armyPrimary().army().name())).findFirst().orElseThrow(NoSuchElementException::new);
                    List<GameArmyEntity> allyArmyEntityList;
                    if (Objects.nonNull(gameSidePlayerData.allyArmyList())) {
                        allyArmyEntityList = new ArrayList<>();
                        gameSidePlayerData.allyArmyList().forEach(allyArmy -> {
                            GameArmyTypeEntity allyArmyTypeEntity = gameArmyTypeEntityList.stream().filter(gameArmyTypeEntity -> gameArmyTypeEntity.getName().equals(allyArmy.name())).findFirst().orElseThrow(NoSuchElementException::new);
                            allyArmyEntityList.add(new GameArmyEntity(
                                    null,
                                    allyArmyTypeEntity,
                                    allyArmy.name(),
                                    allyArmy.pointValue()

                            ));
                        });
                    } else {
                        allyArmyEntityList = null;
                    }
                    gamePlayerDataEntityList.add(
                            new GamePlayerDataEntity(
                                    null,
                                    playerEntity,
                                    new GameArmyEntity(
                                            null,
                                            primaryGameArmyTypeEntity,
                                            gameSidePlayerData.armyPrimary().name(),
                                            gameSidePlayerData.armyPrimary().pointValue()
                                    ),
                                    allyArmyEntityList

                            )
                    );
                }
        );
        return gamePlayerDataEntityList;
    }
}
