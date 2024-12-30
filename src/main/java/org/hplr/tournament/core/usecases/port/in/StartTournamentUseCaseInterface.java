package org.hplr.tournament.core.usecases.port.in;

import java.util.UUID;

public interface StartTournamentUseCaseInterface {
    UUID startTournament(UUID tournamentId);
}
