package org.hplr.game.core.model.vo;

import java.time.Duration;
import java.time.LocalDateTime;

public record GameData(
        GameMission gameMission,
        GameDeployment gameDeployment,
        Long gamePointSize,
        Integer gameTurnLength,
        Duration gameTimeLength,
        LocalDateTime gameStartTime,
        LocalDateTime gameEndTime,
        Boolean ranking
) {
}
