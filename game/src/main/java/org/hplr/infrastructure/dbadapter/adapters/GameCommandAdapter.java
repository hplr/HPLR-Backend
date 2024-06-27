package org.hplr.infrastructure.dbadapter.adapters;

import org.hplr.core.model.GameSnapshot;
import org.hplr.core.usecases.port.out.command.SaveGameCommandInterface;
import org.hplr.exception.HPLRIllegalStateException;
import org.hplr.infrastructure.dbadapter.entities.*;
import org.hplr.infrastructure.dbadapter.mapper.LocationMapper;
import org.hplr.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.infrastructure.dbadapter.repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GameCommandAdapter implements SaveGameCommandInterface {

    final LocationRepository locationRepository;
    final PlayerRepository playerRepository;
    final GameRepository gameRepository;
    final GameMissionRepository gameMissionRepository;
    final GameDeploymentRepository gameDeploymentRepository;
    final GameArmyTypeRepository gameArmyTypeRepository;

    public GameCommandAdapter(LocationRepository locationRepository, PlayerRepository playerRepository, GameRepository gameRepository, GameMissionRepository gameMissionRepository, GameDeploymentRepository gameDeploymentRepository, GameArmyTypeRepository gameArmyTypeRepository) {
        this.locationRepository = locationRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.gameMissionRepository = gameMissionRepository;
        this.gameDeploymentRepository = gameDeploymentRepository;
        this.gameArmyTypeRepository = gameArmyTypeRepository;
    }


    @Override
    public void saveGame(GameSnapshot gameSnapshot) {
        List<PlayerEntity> allPlayerEntityList = playerRepository.findAll();
        if(allPlayerEntityList.isEmpty()){
            throw new HPLRIllegalStateException("Not enough players!");
        }
        List<GameArmyTypeEntity> armyTypeEntityList = gameArmyTypeRepository.findAll();
        if(armyTypeEntityList.isEmpty()){
            throw new HPLRIllegalStateException("No army types!");
        }
        LocationEntity locationEntity = null;
        if(Objects.nonNull(gameSnapshot.gameLocation().location().getLocationId())){
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
}
