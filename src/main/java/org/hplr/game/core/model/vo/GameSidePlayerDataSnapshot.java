package org.hplr.game.core.model.vo;

import org.hplr.elo.core.model.vo.Elo;
import org.hplr.user.core.model.PlayerSnapshot;

import java.util.List;

public record GameSidePlayerDataSnapshot
        (
                PlayerSnapshot player,
                Elo currentELO,
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
