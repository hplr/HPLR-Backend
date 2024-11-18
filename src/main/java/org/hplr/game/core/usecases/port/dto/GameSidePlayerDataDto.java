package org.hplr.game.core.usecases.port.dto;

import org.hplr.game.core.model.vo.GameArmy;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;

import java.util.List;

public record GameSidePlayerDataDto(
        PlayerSelectDto playerSelectDto,
        GameArmy armyPrimary,
        List<GameArmy> allyArmyList
) {
}