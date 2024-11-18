package org.hplr.tournament.core.model;

import org.hplr.tournament.core.model.vo.TournamentData;
import org.hplr.tournament.core.model.vo.TournamentId;
import org.hplr.tournament.core.model.vo.TournamentLocation;
import org.hplr.tournament.core.model.vo.TournamentPlayer;

import java.util.List;

public record TournamentSnapshot(
        TournamentId tournamentId,
        TournamentData tournamentData,
        TournamentLocation tournamentLocation,
        List<TournamentRound>tournamentRoundList,
        List<TournamentPlayer> playerList,
        Boolean closed
) {
    public TournamentSnapshot (Tournament tournament){
        this(
                tournament.getTournamentId(),
                tournament.getTournamentData(),
                tournament.getTournamentLocation(),
                tournament.getTournamentRoundList(),
                tournament.getPlayerList(),
                tournament.getClosed()
        );
    }
}
