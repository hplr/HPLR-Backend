package org.hplr.game.core.model.vo;


public record GameArmy(
        GameArmyType army,
        String name,
        Long pointValue
) {
}
