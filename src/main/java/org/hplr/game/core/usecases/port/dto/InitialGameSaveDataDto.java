package org.hplr.game.core.usecases.port.dto;

import org.hplr.location.core.usecases.port.dto.LocationSaveDto;

public record InitialGameSaveDataDto(
    InitialGameSaveSideDto firstSide,
    InitialGameSaveSideDto secondSide,
    Boolean ranking,
    Long gamePointSize,
    Integer gameTurnLength,
    Integer gameTime,
    String gameStartTime,
    LocationSaveDto locationSaveDto,
    String gameMission,
    String gameDeployment
) {
}
