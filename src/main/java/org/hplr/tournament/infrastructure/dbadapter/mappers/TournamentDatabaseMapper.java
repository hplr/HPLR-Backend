package org.hplr.tournament.infrastructure.dbadapter.mappers;

import org.hplr.game.core.model.Game;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.location.core.model.Location;
import org.hplr.location.infrastructure.dbadapter.mapper.LocationMapper;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentRoundEntity;

public class TournamentDatabaseMapper {

    public static TournamentEntity fromSnapshot(TournamentSnapshot tournamentSnapshot) {
        return new TournamentEntity(
                null,
                tournamentSnapshot.tournamentId().tournamentId(),
                tournamentSnapshot.tournamentData().name(),
                tournamentSnapshot.tournamentData().tournamentStart(),
                tournamentSnapshot.tournamentData().pointSize(),
                tournamentSnapshot.tournamentData().gameLength(),
                tournamentSnapshot.tournamentData().gameTurnAmount(),
                tournamentSnapshot.tournamentData().maxPlayers(),
                LocationMapper.fromSnapshot(tournamentSnapshot.tournamentLocation().location().toSnapshot()),
                tournamentSnapshot.tournamentRoundList()
                        .stream()
                        .map(tournamentRound -> new TournamentRoundEntity(
                                null,
                                tournamentRound.getGameList().stream().map(Game::toSnapshot).map(GameDatabaseMapper::fromSnapshot).toList(),
                                tournamentRound.getGameList().stream().map(Game::toSnapshot).toList()
                        ))
                        .toList(),
                null,
                tournamentSnapshot.closed()
        );
    }
}
