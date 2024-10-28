package org.hplr.game.core.model.vo;


import org.hplr.elo.core.model.vo.Elo;
import org.hplr.user.core.model.Player;

import java.util.List;

public record GameSidePlayerData(
        Player player,
        Elo currentELO,
        GameArmy armyPrimary,
        List<GameArmy> allyArmyList
) {
}
