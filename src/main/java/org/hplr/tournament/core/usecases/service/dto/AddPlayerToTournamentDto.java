package org.hplr.tournament.core.usecases.service.dto;

import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerArmyDto;

import java.util.List;
import java.util.UUID;

public record AddPlayerToTournamentDto(
        UUID tournamentId,
        UUID playerId,
        Allegiance allegiance,
        InitialGameSidePlayerArmyDto primaryArmy,
        List<InitialGameSidePlayerArmyDto> allyArmyList
) {
}
