package org.hplr.tournament.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.location.core.model.Location;
import org.hplr.tournament.core.model.vo.TournamentData;
import org.hplr.tournament.core.model.vo.TournamentId;
import org.hplr.tournament.core.model.vo.TournamentLocation;
import org.hplr.tournament.core.model.vo.TournamentPlayer;
import org.hplr.tournament.core.usecases.service.dto.InitialTournamentSaveDto;
import org.hplr.user.core.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tournament {
    private TournamentId tournamentId;
    private TournamentData tournamentData;
    private TournamentLocation tournamentLocation;
    private List<TournamentRound> tournamentRoundList;
    private List<TournamentPlayer> playerList;
    private Boolean closed;

    public static Tournament fromInitialDto(InitialTournamentSaveDto initialTournamentSaveDto){
        Tournament tournament = new Tournament(
                new TournamentId(UUID.randomUUID()),
                new TournamentData(
                        initialTournamentSaveDto.name(),
                        initialTournamentSaveDto.tournamentStart(),
                        initialTournamentSaveDto.pointLimit(),
                        initialTournamentSaveDto.gameLength(),
                        null,
                        initialTournamentSaveDto.maxPlayers()

                ),
                new TournamentLocation(Location.fromDto(initialTournamentSaveDto.locationSaveDto())),
                new ArrayList<>(),
                new ArrayList<>(),
                false
        );
        //todo: validate
        return tournament;
    }

}
