package org.hplr.game.core.model.vo;

import org.hplr.user.core.model.PlayerSnapshot;

import java.util.List;

public record GameSidePlayerDataSnapshot
        (
                PlayerSnapshot player,
                GameArmy armyPrimary,
                List<GameArmy> allyArmyList
        )
{
        public GameSidePlayerDataSnapshot(GameSidePlayerData gameSidePlayerData) {
                this(
                        new PlayerSnapshot(gameSidePlayerData.player()),
                        gameSidePlayerData.armyPrimary(),
                        gameSidePlayerData.allyArmyList()
                );
        }
}
