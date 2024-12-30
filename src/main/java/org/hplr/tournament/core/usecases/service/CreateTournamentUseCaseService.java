package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.tournament.core.model.Tournament;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.in.CreateTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.port.out.command.SaveTournamentCommandInterface;
import org.hplr.tournament.core.usecases.service.dto.InitialTournamentSaveDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateTournamentUseCaseService implements CreateTournamentUseCaseInterface {

    private final SaveTournamentCommandInterface saveTournamentCommandInterface;

    @Override
    public UUID createTournament(InitialTournamentSaveDto initialTournamentSaveDto) {
        Tournament tournament = Tournament.fromInitialDto(initialTournamentSaveDto);
        TournamentSnapshot tournamentSnapshot = tournament.toSnapshot();
        saveTournamentCommandInterface.saveTournament(tournamentSnapshot);
        return tournamentSnapshot.tournamentId().tournamentId();
    }
}
