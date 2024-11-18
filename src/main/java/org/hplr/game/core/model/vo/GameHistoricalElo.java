package org.hplr.game.core.model.vo;

import org.hplr.elo.core.model.vo.Elo;

public record GameHistoricalElo(
        GameId gameId,
        Elo firstSideElo,
        Elo secondSideElo
) {
}
