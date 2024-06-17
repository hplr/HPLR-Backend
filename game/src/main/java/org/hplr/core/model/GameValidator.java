package org.hplr.core.model;

import java.util.Objects;

public class GameValidator {

    public static void validateCreatedGame(Game game){
        //todo: location adapter
        //todo: finish this class
    }

    private static void validateNoneFirst(Game game){
        if(
                Objects.nonNull(game.getFirstGameSide().getIsFirst()) ||
                Objects.nonNull(game.getSecondGameSide().getIsFirst())
        ) throw new IllegalStateException("Not allowed to set up who is first");
    }

    private static void validateDurationNonNegative(Game game){
        if(!game.getGameData().gameTimeLength().isPositive()){
            throw new IllegalArgumentException("Game duration must be positive");
        }
    }

    private static void validateTurnLengthNonNegative(Game game){
        if(game.getGameData().gameTurnLength()<=0){
            throw new IllegalArgumentException("Game turn length must be positive");
        }
    }
}
