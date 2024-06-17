package org.hplr.core.model;

import org.hplr.core.enums.Status;
import org.hplr.core.model.vo.GameData;
import org.hplr.core.model.vo.GameId;
import org.hplr.core.model.vo.GameLocation;


public record GameSnapshot(
        GameId gameId,
        GameLocation gameLocation,
        GameData gameData,
        Long gameELOChangeValue,
        Status gameStatus,
        GameSide firstGameSide,
        GameSide secondGameSide
) {
    public GameSnapshot(Game game) {
        this(
                game.getGameId(),
                game.getGameLocation(),
                game.getGameData(),
                game.getGameELOChangeValue(),
                game.getGameStatus(),
                game.getFirstGameSide(),
                game.getSecondGameSide()
        );
    }
}
