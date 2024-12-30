package org.hplr.tournament.core.usecases.port.out.command;

import org.hplr.tournament.core.model.TournamentSnapshot;

public interface SaveTournamentCommandInterface {
    void saveTournament(TournamentSnapshot tournamentSnapshot);
}
