package org.hplr.core.usecases.port.dto;

import org.hplr.core.enums.Allegiance;

import java.util.List;

public record InitialGameSaveSideDto(
        Allegiance allegiance,
        List<InitialGameSidePlayerDataDto> playerDataList
) {
}
