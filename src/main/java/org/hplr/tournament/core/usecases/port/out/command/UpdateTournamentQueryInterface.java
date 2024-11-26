package org.hplr.tournament.core.usecases.port.out.command;

import org.hplr.tournament.core.model.TournamentSnapshot;

public interface UpdateTournamentQueryInterface {
    void updateTournament(TournamentSnapshot tournamentSnapshot);
}
