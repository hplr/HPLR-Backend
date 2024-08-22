package org.hplr.core.model;

import org.hplr.core.enums.Status;
import org.hplr.core.model.vo.GameData;
import org.hplr.core.model.vo.GameId;
import org.hplr.core.model.vo.GameLocation;
import org.hplr.core.model.vo.GameSideSnapshot;

import java.util.Objects;


public record GameSnapshot(
        GameId gameId,
        GameLocation gameLocation,
        GameData gameData,
        Long gameELOChangeValue,
        Status gameStatus,
        GameSideSnapshot firstGameSide,
        GameSideSnapshot secondGameSide
) {
    public GameSnapshot(Game game) {
        this(
                game.getGameId(),
                game.getGameLocation(),
                game.getGameData(),
                game.getGameELOChangeValue(),
                game.getGameStatus(),
                new GameSideSnapshot(game.getFirstGameSide()),
                Objects.isNull(game.getSecondGameSide()) ? null : new GameSideSnapshot(game.getSecondGameSide())
        );
    }
}
