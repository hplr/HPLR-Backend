package org.hplr.game.core.usecases.port.dto;


public record InitialGameSidePlayerArmyDto(
        String armyType,
        String armyName,
        Long pointValue
) {
}
