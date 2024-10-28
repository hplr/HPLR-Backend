package org.hplr.game.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.usecases.port.dto.CreatedGameSaveSecondSideDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.game.core.usecases.port.dto.SaveScoreForGameSideDto;
import org.hplr.game.core.usecases.port.in.*;

import org.hplr.library.exception.HPLRValidationException;
import org.hplr.library.exception.LocationCalculationException;

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
    GetAllGamesByStatusAndPlayerIdUseCaseInterface getAllGamesByStatusAndPlayerIdUseCaseInterface;
    SetSecondSideUseCaseInterface setSecondSideUseCaseInterface;
    SaveScoreForGameSideUseCaseInterface saveScoreForGameSideUseCaseInterface;
    FinishGameUseCaseInterface finishGameUseCaseInterface;
    StartGameManualUseCaseInterface startGameManualUseCaseInterface;

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

    @PostMapping(path = "/saveSecondSide")
    public ResponseEntity<UUID> saveGameSecondSide(@RequestBody CreatedGameSaveSecondSideDto createdGameSaveSecondSideDto) {
        UUID gameId;
        //todo: validate
        try {
            gameId = setSecondSideUseCaseInterface.setSecondSideForGame(createdGameSaveSecondSideDto);
        } catch (LocationCalculationException e) {
            throw new LocationCalculationException(e.getMessage());
        } catch (HPLRValidationException e) {
            throw new HPLRValidationException(e.getMessage());
        }
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @PostMapping(path="/saveScore")
    public ResponseEntity<UUID> saveScore(@RequestBody SaveScoreForGameSideDto saveScoreForGameSideDto){
        return new ResponseEntity<>(saveScoreForGameSideUseCaseInterface.saveScoreForGameSide(saveScoreForGameSideDto), HttpStatus.OK);
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

    @GetMapping(path = "/{status}/{playerId}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameSnapshot>> getAllGamesByStatusAndPlayerId(@PathVariable Status status, @PathVariable UUID playerId) {
        return new ResponseEntity<>(getAllGamesByStatusAndPlayerIdUseCaseInterface
                .getAllGamesByStatusAndPlayerId(status,playerId),HttpStatus.OK);
    }

    @PostMapping(path="/finish")
    public ResponseEntity<UUID> finishGame(@RequestParam UUID gameId){
        return new ResponseEntity<>(finishGameUseCaseInterface.finishGame(gameId), HttpStatus.OK);
    }

    @PostMapping("/start")
    public ResponseEntity<UUID> startGame(@RequestParam UUID gameId){
        return new ResponseEntity<>(startGameManualUseCaseInterface.startGameManual(gameId), HttpStatus.OK);
    }
}
