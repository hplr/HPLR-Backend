package org.hplr.tournament.core.usecases.port.out.command;

import org.hplr.tournament.core.model.TournamentSnapshot;

import java.util.UUID;

public interface StartTournamentCommandInterface {
    UUID startTournament(TournamentSnapshot tournamentSnapshot);
}
