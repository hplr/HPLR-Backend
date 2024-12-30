package org.hplr.tournament.core.usecases.port.in;

import org.hplr.tournament.core.usecases.service.dto.InitialTournamentSaveDto;

import java.util.UUID;

public interface CreateTournamentUseCaseInterface {
    UUID createTournament(InitialTournamentSaveDto initialTournamentSaveDto);
}
