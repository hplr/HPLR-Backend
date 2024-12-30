package org.hplr.game.core.model.vo;


import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerArmyDto;

public record GameArmy(
        GameArmyType army,
        String name,
        Long pointValue
) {
    public static GameArmy fromDto(InitialGameSidePlayerArmyDto initialGameSidePlayerArmyDto){
        return new GameArmy(
                new GameArmyType(initialGameSidePlayerArmyDto.armyType()),
                initialGameSidePlayerArmyDto.armyName(),
                initialGameSidePlayerArmyDto.pointValue()
                );
    }
}
