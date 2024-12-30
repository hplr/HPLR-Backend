package org.hplr.tournament.core.usecases.port.dto;

import org.hplr.game.core.usecases.port.dto.GameSelectDto;

import java.util.List;

public record TournamentRoundSelectDto(
        List<GameSelectDto> gameSelectDtoList
) {
}
