package org.hplr.tournament.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.in.AddPlayerToTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.port.in.CreateTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.port.in.GetTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.port.in.StartTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.service.dto.AddPlayerToTournamentDto;
import org.hplr.tournament.core.usecases.service.dto.InitialTournamentSaveDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/tournament")
public class RESTTournamentController {

    private final AddPlayerToTournamentUseCaseInterface addPlayerToTournamentUseCase;
    private final CreateTournamentUseCaseInterface createTournamentUseCase;
    private final GetTournamentUseCaseInterface getTournamentUseCase;
    private final StartTournamentUseCaseInterface startTournamentUseCase;

    @PostMapping(path = "/addPlayerToTournament")
    public ResponseEntity<UUID> addPlayerToTournament(@RequestBody AddPlayerToTournamentDto addPlayerToTournamentDto) {
        return new ResponseEntity<>(addPlayerToTournamentUseCase.addPlayerToTournament(addPlayerToTournamentDto), HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<UUID> createTournament(@RequestBody InitialTournamentSaveDto initialTournamentSaveDto) {
        return new ResponseEntity<>(createTournamentUseCase.createTournament(initialTournamentSaveDto), HttpStatus.OK);
    }

    @GetMapping(path="/{tournamentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TournamentSnapshot> getTournament(@PathVariable UUID tournamentId) {
        return new ResponseEntity<>(getTournamentUseCase.getTournament(tournamentId), HttpStatus.OK);
    }

    @PutMapping(path = "/{tournamentId}")
    public ResponseEntity<UUID> startTournament(@PathVariable UUID tournamentId) {
        return new ResponseEntity<>(startTournamentUseCase.startTournament(tournamentId), HttpStatus.OK);
    }
}
