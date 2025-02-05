package org.hplr.tournament.infrastructure.dbadapter.adapters;

import lombok.AllArgsConstructor;
import org.hplr.game.core.model.GameSide;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.game.core.model.vo.GameSideSnapshot;
import org.hplr.game.infrastructure.dbadapter.adapters.GameCommandAdapter;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.game.infrastructure.dbadapter.mappers.GameSideDatabaseMapper;
import org.hplr.game.infrastructure.dbadapter.repositories.GameArmyTypeRepository;
import org.hplr.game.infrastructure.dbadapter.repositories.GameDeploymentRepository;
import org.hplr.game.infrastructure.dbadapter.repositories.GameMissionRepository;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.mappers.LocationMapper;
import org.hplr.location.infrastructure.dbadapter.repositories.LocationRepository;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.model.vo.TournamentPlayer;
import org.hplr.tournament.core.usecases.port.out.command.SaveTournamentCommandInterface;
import org.hplr.tournament.core.usecases.port.out.command.StartTournamentCommandInterface;
import org.hplr.tournament.core.usecases.port.out.command.UpdateTournamentQueryInterface;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.mappers.TournamentDatabaseMapper;
import org.hplr.tournament.infrastructure.dbadapter.repositories.TournamentRepository;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper.mapScore;

@Service
@AllArgsConstructor
public class TournamentCommandAdapter implements SaveTournamentCommandInterface,
        UpdateTournamentQueryInterface, StartTournamentCommandInterface {

    private final PlayerQueryRepository playerQueryRepository;
    private final GameArmyTypeRepository gameArmyTypeRepository;
    private final TournamentRepository tournamentRepository;
    private final LocationRepository locationRepository;
    private final GameMissionRepository gameMissionRepository;
    private final GameDeploymentRepository gameDeploymentRepository;

    @Override
    public void saveTournament(TournamentSnapshot tournamentSnapshot) {
        TournamentEntity tournamentEntity = TournamentDatabaseMapper.fromSnapshot(tournamentSnapshot);
        tournamentRepository.save(tournamentEntity);
    }

    @Override
    public void updateTournament(TournamentSnapshot tournamentSnapshot) {
        Optional<TournamentEntity> tournamentEntityOptional = tournamentRepository.findByTournamentId(
                tournamentSnapshot.tournamentId().tournamentId());
        if(tournamentEntityOptional.isPresent()){
            if(Objects.equals(tournamentEntityOptional.get().getCurrentPlayers(), tournamentEntityOptional.get().getMaxPlayers())){
                throw new HPLRIllegalStateException("Player limit achieved");
            }
            List<GameSidePlayerData> gameSidePlayerDataList = new ArrayList<>();
            List<GameSideEntity> gameSideEntityList = new ArrayList<>();
            tournamentSnapshot.playerList().forEach(
                    player -> addToGameSideEntityList(player, gameSidePlayerDataList, gameSideEntityList)
            );

            TournamentEntity tournamentEntity = tournamentEntityOptional.get();
            tournamentEntity.setGameSideEntityList(gameSideEntityList);

            tournamentRepository.save(tournamentEntity);

        }
    }

    @Override
    public UUID startTournament(TournamentSnapshot tournamentSnapshot) {
        List<PlayerEntity> allPlayerEntityList = playerQueryRepository.findAll();
        if (allPlayerEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("Not enough players!");
        }
        List<GameArmyTypeEntity> armyTypeEntityList = gameArmyTypeRepository.findAll();
        if (armyTypeEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("No army types!");
        }
        Optional<TournamentEntity> tournamentEntityOptional = tournamentRepository
                .findByTournamentId(tournamentSnapshot.tournamentId().tournamentId());
        TournamentEntity tournamentEntityFromSnapshot = TournamentDatabaseMapper.fromSnapshot(tournamentSnapshot);
        tournamentEntityFromSnapshot.setId(tournamentEntityOptional.orElseThrow(NoSuchElementException::new).getId());
        List<GameSidePlayerData> gameSidePlayerDataList = new ArrayList<>();
        List<GameSideEntity> gameSideEntityList = new ArrayList<>();
        tournamentSnapshot.playerList().forEach(
                player -> addToGameSideEntityList(player, gameSidePlayerDataList, gameSideEntityList)
        );
        tournamentEntityFromSnapshot.setGameSideEntityList(gameSideEntityList);
        tournamentEntityFromSnapshot.getTournamentRoundEntityList().forEach(tournamentRoundEntity ->
                tournamentRoundEntity.getGameSnapshotList().forEach(gameSnapshot -> {
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
                    tournamentRoundEntity.getGameEntityList().add(gameEntity);
                }
        ));
        tournamentRepository.save(tournamentEntityFromSnapshot);
        return tournamentEntityFromSnapshot.getTournamentId();
    }

    public static List<GamePlayerDataEntity> mapGamePlayerDataEntityList(GameSideSnapshot gameSide, List<PlayerEntity> playerEntityList, List<GameArmyTypeEntity> gameArmyTypeEntityList) {
        return GameCommandAdapter.mapGamePlayerDataEntityList(gameSide, playerEntityList, gameArmyTypeEntityList);
    }

    private void addToGameSideEntityList(TournamentPlayer player, List<GameSidePlayerData> gameSidePlayerDataList, List<GameSideEntity> gameSideEntityList){
        List<GamePlayerDataEntity> gamePlayerDataEntityList = new ArrayList<>();
        List<GameArmyEntity> allyArmyEntityList = new ArrayList<>();

        gameSidePlayerDataList.add(player.gameSidePlayerData());
        GameSideEntity gameSideEntity = GameSideDatabaseMapper.fromSnapshot(new GameSideSnapshot(GameSide.fromDto(
                player.allegiance(),
                gameSidePlayerDataList,
                ConstValues.TURN_LENGTH

        )));


        player.gameSidePlayerData().allyArmyList().forEach(army -> allyArmyEntityList.add(
                new GameArmyEntity(
                        null,
                        gameArmyTypeRepository.findByName(army.army().name())
                                .orElseThrow(NoSuchElementException::new),
                        army.name(),
                        army.pointValue()
                )

        ));
        gamePlayerDataEntityList.add(new GamePlayerDataEntity(
                null,
                playerQueryRepository.findByUserId(player.gameSidePlayerData().player().getUserId().id()).orElseThrow(NoSuchElementException::new),
                new GameArmyEntity(
                        null,
                        gameArmyTypeRepository.findByName(player.gameSidePlayerData().armyPrimary().army().name()).orElseThrow(NoSuchElementException::new),
                        player.gameSidePlayerData().armyPrimary().name(),
                        player.gameSidePlayerData().armyPrimary().pointValue()

                ),
                allyArmyEntityList
        ));
        gameSideEntity.setGamePlayerDataEntityList(gamePlayerDataEntityList);
        gameSideEntityList.add(gameSideEntity);
    }
}
