package org.hplr.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.hplr.core.model.PlayerSnapshot;
import org.hplr.core.usecases.port.dto.GetTokenResponseDto;
import org.hplr.core.usecases.port.dto.InitialPlayerSaveDataDto;
import org.hplr.core.usecases.port.dto.PlayerLoginDto;
import org.hplr.core.usecases.port.in.GetAllPlayerListUseCaseInterface;
import org.hplr.core.usecases.port.in.LoginPlayerUseCaseInterface;
import org.hplr.core.usecases.port.in.RegisterPlayerUseCaseInterface;
import org.hplr.core.usecases.service.GetPlayerUseCaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/player")
public class RESTPlayerController {

    final GetAllPlayerListUseCaseInterface getAllPlayerListUseCaseInterface;
    final RegisterPlayerUseCaseInterface registerPlayerUseCaseInterface;
    final LoginPlayerUseCaseInterface loginPlayerUseCaseInterface;
    final GetPlayerUseCaseService getPlayerUseCaseInterface;

    @GetMapping(path = "/getAll", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlayerSnapshot>> getAllPlayerList() {
        return new ResponseEntity<>(getAllPlayerListUseCaseInterface.getAllPlayerList(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerSnapshot> getPlayerByPlayerId(@PathVariable UUID id) {
        return new ResponseEntity<>(getPlayerUseCaseInterface.getPlayerByUserId(id), HttpStatus.OK);
    }

    @PostMapping(path="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> registerPlayer(@RequestBody InitialPlayerSaveDataDto initialPlayerSaveDataDto) {
        return new ResponseEntity<>(registerPlayerUseCaseInterface.registerPlayer(initialPlayerSaveDataDto), HttpStatus.OK);
    }

    @PostMapping(path="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetTokenResponseDto> loginPlayer(@RequestBody PlayerLoginDto playerLoginDto) {
        return new ResponseEntity<>(loginPlayerUseCaseInterface.loginPlayer(playerLoginDto), HttpStatus.OK);
    }
}
