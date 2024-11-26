package org.hplr.tournament.core.usecases.service.dto;

import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.model.vo.GameArmy;

import java.util.List;
import java.util.UUID;

public record AddPlayerToTournamentDto(
        UUID tournamentId,
        UUID playerId,
        Allegiance allegiance,
        GameArmy armyPrimary,
        List<GameArmy> allyArmyList
) {
}
