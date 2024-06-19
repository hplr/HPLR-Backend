package org.hplr.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.core.usecases.port.in.SaveGameUseCaseInterface;
import org.hplr.exception.LocationCalculationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/game")
public class RESTGameController {
    SaveGameUseCaseInterface saveGameUseCaseInterface;

    @PostMapping(path = "/save")
    public ResponseEntity saveGame(@RequestBody InitialGameSaveDataDto initialGameSaveDataDto) throws LocationCalculationException {
        UUID gameId = saveGameUseCaseInterface.saveGame(initialGameSaveDataDto);
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @GetMapping(path = "/debug")
    public ResponseEntity debug() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
