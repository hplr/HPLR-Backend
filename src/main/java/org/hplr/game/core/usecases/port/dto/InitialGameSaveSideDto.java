package org.hplr.game.core.usecases.port.dto;

import org.hplr.game.core.enums.Allegiance;

import java.util.List;


public record InitialGameSaveSideDto(
        Allegiance allegiance,
        List<InitialGameSidePlayerDataDto> playerDataList
) {
}
