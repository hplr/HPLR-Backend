package org.hplr.core.usecases.port.dto;

import org.hplr.core.enums.Allegiance;

import java.util.List;
import java.util.UUID;

public record GameSideSelectDto(
        UUID sideId,
        Allegiance allegiance,
        List<GameSidePlayerDataDto> gameSidePlayerDataList,
        Boolean first,
        List<ScoreDto> scorePerTurnList
) {
}
