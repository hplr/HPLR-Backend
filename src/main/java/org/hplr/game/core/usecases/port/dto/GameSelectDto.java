package org.hplr.game.core.usecases.port.dto;

import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.location.core.usecases.port.dto.LocationSelectDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameSelectDto(
        UUID gameId,
        LocationSelectDto locationSelectDto,
        GameMission gameMission,
        GameDeployment gameDeployment,
        Long gamePointSize,
        Integer gameTurnLength,
        Integer gameHoursDuration,
        LocalDateTime gameStartTime,
        LocalDateTime gameEndTime,
        Boolean ranking,
        Status status,
        GameSideSelectDto firstGameSideSelectDto,
        GameSideSelectDto secondGameSideSelectDto
) {
}
