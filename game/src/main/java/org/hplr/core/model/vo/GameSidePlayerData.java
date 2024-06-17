package org.hplr.core.model.vo;


import org.hplr.core.model.Player;

import java.util.List;

public record GameSidePlayerData(
        Player player,
        ELO currentELO,
        GameArmy armyPrimary,
        List<GameArmy> allyArmyList
) {
}
