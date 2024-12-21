package org.hplr.game.core.model.vo;


import org.hplr.user.core.model.Player;

import java.util.List;
import java.util.Objects;

public record GameSidePlayerData(
        Player player,
        GameArmy armyPrimary,
        List<GameArmy> allyArmyList
) {
    public Long calculateTotalPointValue(){
        return armyPrimary.pointValue() + allyArmyList.stream().map(GameArmy::pointValue).reduce(0L, Long::sum);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GameSidePlayerData that = (GameSidePlayerData) object;
        return Objects.equals(player, that.player) && Objects.equals(armyPrimary, that.armyPrimary) && Objects.equals(allyArmyList, that.allyArmyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, armyPrimary, allyArmyList);
    }
}
