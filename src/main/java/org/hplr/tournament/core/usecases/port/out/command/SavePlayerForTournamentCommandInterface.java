package org.hplr.tournament.core.usecases.port.out.command;


import org.hplr.tournament.core.model.TournamentSnapshot;

public interface SavePlayerForTournamentCommandInterface {
    void savePlayerForTournament(TournamentSnapshot tournamentSnapshot);

}
