package org.hplr.game.core.model.vo;

import org.hplr.elo.core.model.vo.Score;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.model.GameSide;

import java.util.List;

public record GameSideSnapshot
        (
            GameSideId sideId,
            Allegiance allegiance,
            List<GameSidePlayerDataSnapshot> gameSidePlayerDataList,
            Boolean isFirst,
            List<Score> scorePerTurnList
        ){
        public GameSideSnapshot(GameSide gameSide) {
                this(
                        gameSide.getSideId(),
                        gameSide.getAllegiance(),

                        gameSide.getGameSidePlayerDataList().stream().map(GameSidePlayerDataSnapshot::new)
                                .toList(),
                        gameSide.getIsFirst(),
                        gameSide.getScorePerTurnList()
                );
        }
}
