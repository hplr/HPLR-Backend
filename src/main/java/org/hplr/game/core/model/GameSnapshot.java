package org.hplr.game.core.model;

import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.vo.GameData;
import org.hplr.game.core.model.vo.GameId;
import org.hplr.game.core.model.vo.GameLocation;
import org.hplr.game.core.model.vo.GameSideSnapshot;

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
