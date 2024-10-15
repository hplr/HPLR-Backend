package org.hplr.core.usecases.port.dto;

import java.util.UUID;

public record SaveScoreForGameSideDto(
        UUID gameId,
        UUID gameSideId,
        Long turnNumber,
        Long score,
        Boolean table
) {
}
