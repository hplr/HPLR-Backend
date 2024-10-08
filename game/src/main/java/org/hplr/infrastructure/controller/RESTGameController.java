package org.hplr.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.model.vo.GameArmyType;
import org.hplr.core.model.vo.GameDeployment;
import org.hplr.core.model.vo.GameMission;
import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.core.usecases.port.in.*;
import org.hplr.exception.HPLRValidationException;
import org.hplr.exception.LocationCalculationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/game")
public class RESTGameController {
    SaveGameUseCaseInterface saveGameUseCaseInterface;
    GetGameByIDUseCaseInterface getGameByIDUseCaseInterface;
    GetAllGameArmyTypesUseCaseInterface getAllGameArmyTypesUseCaseInterface;
    GetAllGameMissionsUseCaseInterface getAllGameMissionsUseCaseInterface;
    GetAllGameDeploymentsUseCaseInterface getAllGameDeploymentsUseCaseInterface;

    @PostMapping(path = "/save")
    public ResponseEntity<UUID> saveGame(@RequestBody InitialGameSaveDataDto initialGameSaveDataDto) {
        UUID gameId;
        try {
            gameId = saveGameUseCaseInterface.saveGame(initialGameSaveDataDto);
        } catch (LocationCalculationException e) {
            throw new LocationCalculationException(e.getMessage());
        } catch (HPLRValidationException e) {
            throw new HPLRValidationException(e.getMessage());
        }
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}",  produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameSnapshot> getGameById(@PathVariable UUID id) {
        GameSnapshot gameSnapshot;

        gameSnapshot = getGameByIDUseCaseInterface.getGameByID(id);
        return new ResponseEntity<>(gameSnapshot, HttpStatus.OK);
    }

    @GetMapping(path = "/army", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameArmyType>> getAllGameArmyType() {
        return new ResponseEntity<>(getAllGameArmyTypesUseCaseInterface.getAllGameArmyTypes(),HttpStatus.OK);
    }

    @GetMapping(path = "/mission", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameMission>> getAllGameMissions() {
        return new ResponseEntity<>(getAllGameMissionsUseCaseInterface.getAllGameMissions(),HttpStatus.OK);
    }

    @GetMapping(path = "/deployment", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDeployment>> getAllGameDeployments() {
        return new ResponseEntity<>(getAllGameDeploymentsUseCaseInterface.getAllGameDeployments(),HttpStatus.OK);
    }
}
