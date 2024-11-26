package org.hplr.tournament.core.usecases.port.dto;

import org.hplr.game.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.location.core.usecases.port.dto.LocationSelectDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TournamentSelectDto(
        UUID tournamentId,
        String tournamentName,
        LocalDateTime tournamentStart,
        Long pointLimit,
        Integer gameLength,
        Integer gameTurnAmount,
        Integer maxPlayers,
        LocationSelectDto locationSelectDto,
        List<TournamentRoundSelectDto> tournamentRoundSelectDtoList,
        List<GameSideSelectDto> playerDtoList,
        Boolean closed
) {

}
