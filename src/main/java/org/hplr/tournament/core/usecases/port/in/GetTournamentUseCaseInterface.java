package org.hplr.tournament.core.usecases.port.in;

import org.hplr.tournament.core.model.TournamentSnapshot;

import java.util.UUID;

public interface GetTournamentUseCaseInterface {
    TournamentSnapshot getTournament(UUID tournamentId);
}
