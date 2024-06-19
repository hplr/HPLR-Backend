package org.hplr.core.model.vo;


public record GameArmy(
        GameArmyType army,
        String name,
        Long pointValue
) {
}
