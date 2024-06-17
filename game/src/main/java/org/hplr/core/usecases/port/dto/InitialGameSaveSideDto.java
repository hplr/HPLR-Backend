package org.hplr.core.usecases.port.dto;

import org.hplr.core.enums.Allegiance;
import org.hplr.core.model.vo.GameArmy;

import java.util.List;
import java.util.UUID;

public record InitialGameSaveSideDto(
        Allegiance allegiance,
        List<InitialGameSidePlayerDataDto> playerDataList
) {
}
