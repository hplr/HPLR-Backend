package org.hplr.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.hplr.core.model.PlayerSnapshot;
import org.hplr.core.usecases.port.in.GetAllPlayerListUseCaseInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/player")
public class RESTPlayerController {

    final GetAllPlayerListUseCaseInterface getAllPlayerListUseCaseInterface;
    @GetMapping(path = "/getAll", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlayerSnapshot>> getAllPlayerList() {
        return new ResponseEntity<>(getAllPlayerListUseCaseInterface.getAllPlayerList(), HttpStatus.OK);
    }
}
