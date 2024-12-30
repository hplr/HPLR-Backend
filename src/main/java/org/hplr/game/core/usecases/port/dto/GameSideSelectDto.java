package org.hplr.game.core.usecases.port.dto;

import org.hplr.elo.core.usecases.port.dto.ScoreDto;
import org.hplr.game.core.enums.Allegiance;

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
