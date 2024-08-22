package org.hplr.core.model.vo;

import org.hplr.core.model.PlayerSnapshot;

import java.util.List;

public record GameSidePlayerDataSnapshot
        (
                PlayerSnapshot player,
                ELO currentELO,
                GameArmy armyPrimary,
                List<GameArmy> allyArmyList
        )
{
        public GameSidePlayerDataSnapshot(GameSidePlayerData gameSidePlayerData) {
                this(
                        new PlayerSnapshot(gameSidePlayerData.player()),
                        gameSidePlayerData.currentELO(),

                        gameSidePlayerData.armyPrimary(),
                        gameSidePlayerData.allyArmyList()
                );
        }
}
