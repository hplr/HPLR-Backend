package org.hplr.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.infrastructure.dbadapter.repositories.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/debug")
public class RESTDebugController {

    PlayerRepository playerRepository;

    @GetMapping(path = "addPlayer/{playerId}/{email}")
    public ResponseEntity debugAddPlayer(@PathVariable UUID playerId, @PathVariable String email){
        playerRepository.save(
                new PlayerEntity(
                        playerId,
                        "Jan",
                        email,
                        0L


                )
        );
        return new ResponseEntity(HttpStatus.OK);
    }
}
