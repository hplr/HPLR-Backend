package org.hplr.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.core.usecases.port.in.GetGameByIDUseCaseInterface;
import org.hplr.core.usecases.port.in.SaveGameUseCaseInterface;
import org.hplr.exception.HPLRIllegalArgumentException;
import org.hplr.exception.HPLRIllegalStateException;
import org.hplr.exception.LocationCalculationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/game")
public class RESTGameController {
    private static final Logger log = LoggerFactory.getLogger(RESTGameController.class);
    SaveGameUseCaseInterface saveGameUseCaseInterface;
    GetGameByIDUseCaseInterface getGameByIDUseCaseInterface;

    @PostMapping(path = "/save")
    public ResponseEntity saveGame(@RequestBody InitialGameSaveDataDto initialGameSaveDataDto) {
        UUID gameId;
        try {
            gameId = saveGameUseCaseInterface.saveGame(initialGameSaveDataDto);
        }catch(LocationCalculationException e){
            throw new LocationCalculationException(e.getMessage());
        }catch (HPLRIllegalStateException e){
            throw new HPLRIllegalStateException(e.getMessage());
        }
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GameSnapshot> getGameById(@PathVariable UUID id) {
        GameSnapshot gameSnapshot = null;
        try{
            gameSnapshot = getGameByIDUseCaseInterface.getGameByID(id);
        } catch(Exception e){
            log.error("error");
        }
        return new ResponseEntity<>(gameSnapshot, HttpStatus.OK);
    }
}
