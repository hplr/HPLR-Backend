package org.hplr.tournament.core.model.dto;

import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.location.core.model.Location;
import org.hplr.tournament.core.model.vo.TournamentPlayer;

public record TournamentGameDto(
        TournamentPlayer firstSide,
        TournamentPlayer secondSide,
        Long gamePointSize,
        Integer gameTurnLength,
        String gameStartTime,
        String gameEndTime,
        Integer gameHoursDuration,
        Location location,
        GameMission gameMission,
        GameDeployment gameDeployment
) {
}
