package org.hplr.core.usecases.port.dto;

import java.time.LocalDateTime;

public record InitialGameSaveDataDto(
    InitialGameSaveSideDto firstSide,
    InitialGameSaveSideDto secondSide,
    Boolean ranking,
    Long gamePointSize,
    Integer gameTurnLength,
    Integer gameTime,
    LocalDateTime gameStartTime,
    LocationSaveDto locationSaveDto,
    String gameMission,
    String gameDeployment
) {
}
