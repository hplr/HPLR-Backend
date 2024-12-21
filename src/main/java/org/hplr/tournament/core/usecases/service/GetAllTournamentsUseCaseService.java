package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.tournament.core.model.Tournament;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;
import org.hplr.tournament.core.usecases.port.in.GetAllTournamentsUseCaseInterface;
import org.hplr.tournament.core.usecases.port.out.query.SelectAllTournamentsQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllTournamentsUseCaseService implements GetAllTournamentsUseCaseInterface {

    private final SelectAllTournamentsQueryInterface selectAllTournamentsQueryInterface;

    @Override
    public List<TournamentSnapshot> getAllTournamentsList(Boolean closed) {
        List<TournamentSelectDto> tournamentSelectDtoList = selectAllTournamentsQueryInterface.selectAllTournaments(closed);
        List<Tournament> tournamentList = tournamentSelectDtoList.stream().map(Tournament::fromSelectDto).toList();
        return tournamentList.stream().map(Tournament::toSnapshot).toList();
    }
}
