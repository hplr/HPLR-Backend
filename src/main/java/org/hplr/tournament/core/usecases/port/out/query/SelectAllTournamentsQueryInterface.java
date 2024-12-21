package org.hplr.tournament.core.usecases.port.out.query;

import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;

import java.util.List;

public interface SelectAllTournamentsQueryInterface {
    List<TournamentSelectDto> selectAllTournaments(Boolean closed);
}
