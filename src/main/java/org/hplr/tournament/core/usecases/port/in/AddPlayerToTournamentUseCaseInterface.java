package org.hplr.tournament.core.usecases.port.in;

import org.hplr.tournament.core.usecases.service.dto.AddPlayerToTournamentDto;

import java.util.UUID;

public interface AddPlayerToTournamentUseCaseInterface {
    UUID addPlayerToTournament(AddPlayerToTournamentDto addPlayerToTournamentDto);
}
