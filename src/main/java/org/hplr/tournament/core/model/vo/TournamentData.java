package org.hplr.tournament.core.model.vo;

import java.time.LocalDateTime;

public record TournamentData(
        String name,
        LocalDateTime tournamentStart,
        Long pointSize,
        Integer gameLength,
        Integer gameTurnAmount,
        Integer maxPlayers
) {
}
