package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.tournament.core.model.Tournament;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;
import org.hplr.tournament.core.usecases.port.in.GetTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.port.out.query.SelectTournamentByTournamentIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetTournamentUseCaseService implements GetTournamentUseCaseInterface {

    private final SelectTournamentByTournamentIdQueryInterface selectTournamentByTournamentIdQueryInterface;

    @Override
    public TournamentSnapshot getTournament(UUID tournamentId) {
        Optional<TournamentSelectDto> tournamentSelectDto = selectTournamentByTournamentIdQueryInterface
                .selectTournamentByTournamentId(tournamentId);
        Tournament tournament = Tournament.fromSelectDto(tournamentSelectDto.orElseThrow(NoSuchElementException::new));
        return tournament.toSnapshot();
    }
}
