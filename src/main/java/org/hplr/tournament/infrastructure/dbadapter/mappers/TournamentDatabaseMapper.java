package org.hplr.tournament.infrastructure.dbadapter.mappers;

import org.hplr.game.core.model.Game;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.game.infrastructure.dbadapter.mappers.GameSideDatabaseMapper;
import org.hplr.location.infrastructure.dbadapter.mapper.LocationMapper;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.dto.TournamentRoundSelectDto;
import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentRoundEntity;

import java.util.ArrayList;


public class TournamentDatabaseMapper {
    private TournamentDatabaseMapper(){
        throw new IllegalStateException("Utility class");
    }
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
                                new ArrayList<>(),
                                tournamentRound.getGameList().stream().map(Game::toSnapshot).toList()
                        ))
                        .toList(),
                null,
                tournamentSnapshot.closed()
        );
    }

    public static TournamentSelectDto fromEntity(TournamentEntity tournamentEntity) {
        return new TournamentSelectDto(
                tournamentEntity.getTournamentId(),
                tournamentEntity.getName(),
                tournamentEntity.getTournamentStart(),
                tournamentEntity.getPointSize(),
                tournamentEntity.getGameLength(),
                tournamentEntity.getGameTurnAmount(),
                tournamentEntity.getMaxPlayers(),
                LocationMapper.fromEntity(tournamentEntity.getLocationEntity()),
                tournamentEntity.getTournamentRoundEntityList()
                        .stream()
                        .map(tournamentRoundEntity ->
                                new TournamentRoundSelectDto(tournamentRoundEntity.getGameEntityList()
                                        .stream()
                                        .map(GameDatabaseMapper::toDto)
                                        .toList()
                                )).toList(),
                tournamentEntity.getGameSideEntityList().stream().map(GameSideDatabaseMapper::fromEntity).toList(),
                tournamentEntity.getClosed()
        );
    }
}

