package org.hplr.core.usecases.port.dto;

import org.hplr.core.enums.Allegiance;

import java.util.List;
import java.util.UUID;

public record CreatedGameSaveSecondSideDto(
        UUID gameId,
        Allegiance allegiance,
        List<InitialGameSidePlayerDataDto> playerDataList
) {
}
