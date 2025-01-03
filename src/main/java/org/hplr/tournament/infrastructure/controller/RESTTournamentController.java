package org.hplr.tournament.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hplr.library.exception.HPLRAccessDeniedException;
import org.hplr.library.infrastructure.controller.AccessValidator;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.in.*;
import org.hplr.tournament.core.usecases.service.dto.AddPlayerToTournamentDto;
import org.hplr.tournament.core.usecases.service.dto.InitialTournamentSaveDto;
import org.hplr.user.core.model.vo.AdministratorRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/tournament")
@Tag(name = "Tournament")
public class RESTTournamentController {

    private final AddPlayerToTournamentUseCaseInterface addPlayerToTournamentUseCase;
    private final CreateTournamentUseCaseInterface createTournamentUseCase;
    private final GetTournamentUseCaseInterface getTournamentUseCase;
    private final GetAllTournamentsUseCaseInterface getAllTournamentsUseCase;
    private final StartTournamentUseCaseInterface startTournamentUseCase;
    private final AccessValidator accessValidator;

    @PostMapping(path = "/addPlayerToTournament")
    @Operation(summary = "Adds player to the tournament", description = "Player is added to the tournament with all the necessary data")
    public ResponseEntity<UUID> addPlayerToTournament(@RequestBody AddPlayerToTournamentDto addPlayerToTournamentDto) {
        return new ResponseEntity<>(addPlayerToTournamentUseCase.addPlayerToTournament(addPlayerToTournamentDto), HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    @Operation(summary = "Creates empty tournament", description = "Tournament is initialized without any players added")
    public ResponseEntity<UUID> createTournament(@RequestBody InitialTournamentSaveDto initialTournamentSaveDto) {
        return new ResponseEntity<>(createTournamentUseCase.createTournament(initialTournamentSaveDto), HttpStatus.OK);
    }

    @GetMapping(path="/{tournamentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Fetches tournament data", description = "Tournament data is returned to the requesting body")
    public ResponseEntity<TournamentSnapshot> getTournament(@PathVariable UUID tournamentId) {
        return new ResponseEntity<>(getTournamentUseCase.getTournament(tournamentId), HttpStatus.OK);
    }

    @GetMapping(path="/tournamentList", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Fetches list of tournament ids", description = "Tournament ids list is returned to the requesting body")
    public ResponseEntity<List<TournamentSnapshot>> getTournamentList(@RequestParam boolean closed) {
        return new ResponseEntity<>(getAllTournamentsUseCase.getAllTournamentsList(closed), HttpStatus.OK);
    }

    @PutMapping(path = "/{tournamentId}")
    @Operation(summary = "Starts tournament", description = "Tournament is started by the Administrator")
    public ResponseEntity<UUID> startTournament(HttpServletRequest request, @PathVariable UUID tournamentId) {
        if(Boolean.FALSE.equals(accessValidator.validateAdministratorAccess(request, AdministratorRole.TOURNAMENT_START))){
            throw new HPLRAccessDeniedException("No permissions!");
        }
        return new ResponseEntity<>(startTournamentUseCase.startTournament(tournamentId), HttpStatus.OK);
    }
}
