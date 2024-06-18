package org.hplr.core.usecases.port.dto;

import org.hplr.core.enums.Allegiance;
import org.hplr.core.model.vo.GameSidePlayerData;
import org.hplr.core.model.vo.Score;

import java.util.List;
import java.util.UUID;

public record GameSideSelectDto(
        UUID sideId,
        Allegiance allegiance,
        List<GameSidePlayerData> gameSidePlayerDataList,
        Boolean first,
        List<Score> scorePerTurnList
) {
}
