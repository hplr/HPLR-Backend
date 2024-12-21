package org.hplr.tournament.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;
import org.hplr.tournament.core.usecases.port.out.query.SelectAllTournamentsQueryInterface;
import org.hplr.tournament.core.usecases.port.out.query.SelectTournamentByTournamentIdQueryInterface;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.mappers.TournamentDatabaseMapper;
import org.hplr.tournament.infrastructure.dbadapter.repositories.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TournamentQueryAdapter implements SelectTournamentByTournamentIdQueryInterface,
        SelectAllTournamentsQueryInterface {

    private final TournamentRepository tournamentRepository;

    @Override
    public Optional<TournamentSelectDto> selectTournamentByTournamentIdQueryInterface(UUID tournamentId) {
        TournamentEntity tournamentEntity = tournamentRepository.findByTournamentId(tournamentId).orElseThrow(NoSuchElementException::new);
        return Optional.of(TournamentDatabaseMapper.fromEntity(tournamentEntity));
    }

    @Override
    public List<TournamentSelectDto> selectAllTournaments(Boolean closed) {
        return tournamentRepository.findAllByClosed(closed).stream().map(
                TournamentDatabaseMapper::fromEntity).toList();
    }
}
