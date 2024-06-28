package org.hplr.core.usecases.port.dto;

import org.hplr.core.model.vo.GameArmy;

import java.util.List;

public record GameSidePlayerDataDto(
        PlayerSelectDto playerSelectDto,
        ELODto currentELO,
        GameArmy armyPrimary,
        List<GameArmy> allyArmyList
) {
}