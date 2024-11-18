package org.hplr.tournament.core.model.vo;

import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.model.vo.GameSidePlayerData;

public record TournamentPlayer(
        Allegiance allegiance,
        GameSidePlayerData gameSidePlayerData
) {
}
