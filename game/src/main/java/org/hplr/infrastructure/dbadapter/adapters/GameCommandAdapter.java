package org.hplr.infrastructure.dbadapter.adapters;

import lombok.extern.slf4j.Slf4j;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.usecases.port.out.command.SaveFinishedGameCommandInterface;
import org.hplr.core.usecases.port.out.command.SaveGameCommandInterface;
import org.hplr.core.usecases.port.out.command.StartAllDueGamesCommandInterface;
import org.hplr.exception.HPLRIllegalStateException;
import org.hplr.infrastructure.dbadapter.entities.*;
import org.hplr.infrastructure.dbadapter.mapper.LocationMapper;
import org.hplr.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.infrastructure.dbadapter.repositories.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GameCommandAdapter implements SaveGameCommandInterface,
        StartAllDueGamesCommandInterface,
        SaveFinishedGameCommandInterface {

    final LocationRepository locationRepository;
    final PlayerQueryRepository playerQueryRepository;
    final GameRepository gameRepository;
    final GameMissionRepository gameMissionRepository;
    final GameDeploymentRepository gameDeploymentRepository;
    final GameArmyTypeRepository gameArmyTypeRepository;

    public GameCommandAdapter(LocationRepository locationRepository, PlayerQueryRepository playerQueryRepository, GameRepository gameRepository, GameMissionRepository gameMissionRepository, GameDeploymentRepository gameDeploymentRepository, GameArmyTypeRepository gameArmyTypeRepository) {
        this.locationRepository = locationRepository;
        this.playerQueryRepository = playerQueryRepository;
        this.gameRepository = gameRepository;
        this.gameMissionRepository = gameMissionRepository;
        this.gameDeploymentRepository = gameDeploymentRepository;
        this.gameArmyTypeRepository = gameArmyTypeRepository;
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

        gameRepository.save(GameDatabaseMapper.fromSnapshot(gameSnapshot, locationEntity, gameMissionEntity, gameDeploymentEntity, allPlayerEntityList, armyTypeEntityList));
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
    public void saveFinishedGame(GameSnapshot gameSnapshot) {
        GameEntity gameEntity = gameRepository.findByGameId(gameSnapshot.gameId().gameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        gameEntity.setStatus(gameSnapshot.gameStatus());
        gameRepository.save(gameEntity);
    }
}
