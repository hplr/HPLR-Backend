package org.hplr.tournament.core.model;

import org.hplr.library.core.model.StringValidator;
import org.hplr.library.exception.HPLRIllegalArgumentException;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.tournament.core.model.vo.TournamentPlayer;
import org.hplr.user.core.model.Player;

import java.time.LocalDateTime;
import java.util.Objects;

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
        if(0>=tournamentPlayer.gameSidePlayerData().calculateTotalPointValue()){
            throw new HPLRIllegalStateException("Declared point size must be non-negative!");
        }
    }

    public static void validateIfTournamentCanBeStarted(Tournament tournament){
        if(tournament.getPlayerList().size()<4){
            throw new HPLRIllegalStateException("Not enough players to start tournament!");
        }
    }

    public static void validateIfNameIsCorrect(Tournament tournament){
        StringValidator.validateString(tournament.getTournamentData().name());
    }

    public static void validateIfStartDateIsCorrect(Tournament tournament){
        if(tournament.getTournamentData().tournamentStart().isBefore(LocalDateTime.now())){
            throw new HPLRValidationException("Start date cannot be past!");
        }
    }

    public static void validateIfPointLimitIsCorrect(Tournament tournament){
        if(tournament.getTournamentData().pointSize()<=0){
            throw new HPLRValidationException("Point limit cannot be non-positive!");
        }
    }

    public static void validateIfGameLengthIsCorrect(Tournament tournament){
        if(tournament.getTournamentData().gameLength()<=0){
            throw new HPLRValidationException("Game length cannot be non-positive!");
        }
    }

    public static void validateIfGameTurnAmountIsCorrect(Tournament tournament){
        if(tournament.getTournamentData().gameTurnAmount()<=0){
            throw new HPLRValidationException("Game turn amount cannot be non-positive!");
        }
    }

    public static void validateIfPlayerLimitIsCorrect(Tournament tournament){
        if(tournament.getTournamentData().maxPlayers()<=0){
            throw new HPLRValidationException("Player limit cannot be non-positive!");
        }
    }

    public static void validateLocation(Tournament tournament){
        if(Objects.isNull(tournament.getTournamentLocation().location())){
            throw new HPLRValidationException("Location must be provided!");
        }
    }

    public static void validateIfTournamentSizeIsNotReached(Tournament tournament){
        if(Objects.equals(tournament.getPlayerList().size(), tournament.getTournamentData().maxPlayers())){
            throw new HPLRValidationException("Location must be provided!");
        }
    }

    public static void validateInitialTournament(Tournament tournament){
        validateIfNameIsCorrect(tournament);
        validateIfStartDateIsCorrect(tournament);
        validateIfPointLimitIsCorrect(tournament);
        validateIfGameLengthIsCorrect(tournament);
        validateIfGameTurnAmountIsCorrect(tournament);
        validateIfPlayerLimitIsCorrect(tournament);
        validateLocation(tournament);
    }
}
