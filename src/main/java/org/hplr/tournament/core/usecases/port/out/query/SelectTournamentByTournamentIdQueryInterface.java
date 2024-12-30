package org.hplr.tournament.core.usecases.port.out.query;

import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;

import java.util.Optional;
import java.util.UUID;

public interface SelectTournamentByTournamentIdQueryInterface {
    Optional<TournamentSelectDto> selectTournamentByTournamentId(UUID tournamentId);
}
