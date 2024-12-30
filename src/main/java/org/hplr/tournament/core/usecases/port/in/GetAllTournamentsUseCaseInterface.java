package org.hplr.tournament.core.usecases.port.in;

import org.hplr.tournament.core.model.TournamentSnapshot;

import java.util.List;

public interface GetAllTournamentsUseCaseInterface {
    List<TournamentSnapshot> getAllTournamentsList(Boolean closed);
}
