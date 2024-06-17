package org.hplr.core.usecases.port.dto;

import java.util.List;
import java.util.UUID;

public record InitialGameSidePlayerDataDto(
        UUID playerId,
        InitialGameSidePlayerArmyDto primaryArmy,
        List<InitialGameSidePlayerArmyDto> allyArmyList
) {
}
