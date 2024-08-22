package org.hplr.core.model.vo;

import org.hplr.core.enums.Allegiance;
import org.hplr.core.model.GameSide;

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
