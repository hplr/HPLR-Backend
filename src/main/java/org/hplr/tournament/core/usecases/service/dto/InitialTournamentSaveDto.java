package org.hplr.tournament.core.usecases.service.dto;

import org.hplr.location.core.usecases.port.dto.LocationSaveDto;

import java.time.LocalDateTime;

public record InitialTournamentSaveDto(
        String name,
        LocalDateTime tournamentStart,
        Long pointLimit,
        Integer gameLength,
        Integer gameTurnAmount,
        Integer maxPlayers,
        LocationSaveDto locationSaveDto
) {
}
