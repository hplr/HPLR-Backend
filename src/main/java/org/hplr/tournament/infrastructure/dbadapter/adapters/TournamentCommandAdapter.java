package org.hplr.tournament.infrastructure.dbadapter.adapters;

import lombok.AllArgsConstructor;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.out.command.SaveTournamentCommandInterface;
import org.hplr.tournament.core.usecases.port.out.command.UpdateTournamentQueryInterface;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.mappers.TournamentDatabaseMapper;
import org.hplr.tournament.infrastructure.dbadapter.repositories.TournamentRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TournamentCommandAdapter implements SaveTournamentCommandInterface,
        UpdateTournamentQueryInterface {

    private final TournamentRepository tournamentRepository;

    @Override
    public void saveTournament(TournamentSnapshot tournamentSnapshot) {
        TournamentEntity tournamentEntity = TournamentDatabaseMapper.fromSnapshot(tournamentSnapshot);
        tournamentRepository.save(tournamentEntity);
    }

    @Override
    public void updateTournament(TournamentSnapshot tournamentSnapshot) {
        TournamentEntity tournamentEntity = TournamentDatabaseMapper.fromSnapshot(tournamentSnapshot);
        tournamentRepository.save(tournamentEntity);
    }
}
