package org.hplr.core.usecases.port.dto;

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
