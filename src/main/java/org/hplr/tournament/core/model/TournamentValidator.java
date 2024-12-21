package org.hplr.tournament.core.model;

import org.hplr.library.exception.HPLRIllegalArgumentException;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.tournament.core.model.vo.TournamentPlayer;
import org.hplr.user.core.model.Player;

public class TournamentValidator {
    private TournamentValidator(){
        throw new IllegalStateException("Utility class");
    }
    public static void validateIfPlayerIsNotInTheTournament(Tournament tournament, Player player) throws HPLRIllegalArgumentException {
        if (tournament.getPlayerList()
                .stream()
                .map(lambdaPlayer -> lambdaPlayer.gameSidePlayerData().player())
                .toList()
                .stream()
                .anyMatch(lambdaPlayer -> player.getUserId().id().equals(lambdaPlayer.getUserId().id()))
        ) {
            throw new HPLRIllegalArgumentException("Player is already registered for this tournament");
        }
    }

    public static void validateIfPointSizeIsLegal(Tournament tournament, TournamentPlayer tournamentPlayer) throws HPLRIllegalStateException{
        if(tournament.getTournamentData().pointSize()<tournamentPlayer.gameSidePlayerData().calculateTotalPointValue()){
            throw new HPLRIllegalStateException("Declared point size is over the limit!");
        }
    }

    public static void validateIfTournamentCanBeStarted(Tournament tournament){
        if(tournament.getPlayerList().size()<4){
            throw new HPLRIllegalStateException("Not enough players to start tournament!");
        }
    }
}
