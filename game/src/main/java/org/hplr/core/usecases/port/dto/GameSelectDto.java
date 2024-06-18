package org.hplr.core.usecases.port.dto;

import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import org.hplr.core.enums.Status;
import org.hplr.core.model.vo.GameDeployment;
import org.hplr.core.model.vo.GameMission;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameSelectDto(
        UUID gameId,
        LocationSelectDto locationSelectDto,
        GameMission gameMission,
        GameDeployment gameDeployment,
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
