package org.hplr.game.core.model;

import org.hplr.game.core.model.vo.GameArmy;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRIllegalArgumentException;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameValidator {

    public static void validateCreatedStandaloneGame(Game game) throws HPLRValidationException {
        try {
            validateNoneFirst(game);
            validateMandatoryGameInfo(game);
        } catch (HPLRIllegalStateException | HPLRIllegalArgumentException e) {
            throw new HPLRValidationException(e.toString());
        }
    }

    private static void validateNoneFirst(Game game) {
        if (
                Objects.nonNull(game.getFirstGameSide().getIsFirst()) ||
                        (
                                Objects.nonNull(game.getSecondGameSide()) &&
                                        Objects.nonNull(game.getSecondGameSide().getIsFirst())
                        )
        ) throw new HPLRIllegalStateException("Not allowed to set up who is first");
    }

    private static void validateDurationNonNegative(Game game) {
        if (!game.getGameData().gameTimeLength().isPositive()) {
            throw new HPLRIllegalArgumentException("Game duration must be positive");

        }
    }

    private static void validateTurnLengthNonNegative(Game game) {
        if (game.getGameData().gameTurnLength() <= 0) {
            throw new HPLRIllegalArgumentException("Game turn length must be positive");
        }
    }

    private static void validateRankingGameHasFullData(Game game) {
        if (Boolean.TRUE.equals(game.getGameData().ranking()) && Objects.isNull(game.getSecondGameSide())) {
            throw new HPLRIllegalStateException("Ranked game must have defined both sides");
        }
    }

    private static void validateRankingGameHasCorrectPlayers(Game game) {
        boolean hasDefaultPlayer = false;
        for (GameSidePlayerData gameSidePlayerData : game.getFirstGameSide().getGameSidePlayerDataList()) {
            if (gameSidePlayerData.player().getUserId().id().equals(ConstValues.DEFAULT_PLAYER_ID)) {
                hasDefaultPlayer = true;
                break;
            }
        }
        if (Objects.nonNull(game.getSecondGameSide())) {
            for (GameSidePlayerData gameSidePlayerData : game.getSecondGameSide().getGameSidePlayerDataList()) {
                if (gameSidePlayerData.player().getUserId().id().equals(ConstValues.DEFAULT_PLAYER_ID)) {
                    hasDefaultPlayer = true;
                    break;
                }
            }
        }

        if (Boolean.TRUE.equals(game.getGameData().ranking()) && Boolean.TRUE.equals(hasDefaultPlayer)) {
            throw new HPLRIllegalStateException("Ranked game must cannot be played by anonymous players");
        }
    }

    private static void validateRankingGameHasNoPlayerOnBothSides(Game game) {
        List<UUID> firstSidePlayerIdList = game.getFirstGameSide().getGameSidePlayerDataList().stream().map(gameSidePlayerData -> gameSidePlayerData.player().getUserId().id()).toList();
        List<UUID> secondSidePlayerIdList = game.getSecondGameSide().getGameSidePlayerDataList().stream().map(gameSidePlayerData -> gameSidePlayerData.player().getUserId().id()).toList();
        if (firstSidePlayerIdList.stream().anyMatch(secondSidePlayerIdList::contains) || secondSidePlayerIdList.stream().anyMatch(firstSidePlayerIdList::contains)) {
            throw new HPLRIllegalStateException("Ranked game cannot have same player on both sides");
        }
    }


    private static void validateSideHasCorrectAmountOfPoints(Game game, GameSide gameSide) {
        if (Objects.nonNull(gameSide)) {
            Long pointsSum = 0L;
            for (GameSidePlayerData gameSidePlayerData : gameSide.getGameSidePlayerDataList()) {
                pointsSum += gameSidePlayerData.armyPrimary().pointValue();
                if (Objects.nonNull(gameSidePlayerData.allyArmyList())) {
                    for (GameArmy gameArmy : gameSidePlayerData.allyArmyList()) {
                        pointsSum += gameArmy.pointValue();
                    }
                }

            }
            if (pointsSum <= 0) {
                throw new HPLRIllegalStateException("No correct armies declared");
            }
            if (pointsSum > game.getGameData().gamePointSize()) {
                throw new HPLRIllegalStateException("Side has exceeded points limit");
            }
        }

    }

    private static void validateSideIsNotNull(GameSide gameSide) {
        if (Objects.isNull(gameSide)) {
            throw new HPLRIllegalStateException("Side must not be null");
        }
    }

    private static void validateSideHasFilledTurnPoints(GameSide gameSide) {
        Boolean emptyScore = gameSide.getScorePerTurnList()
                .stream()
                .anyMatch(
                        score -> Objects.isNull(score.turnScore()) || score.turnScore().equals(0L)
                );
        if (Boolean.TRUE.equals(emptyScore)) {
            throw new HPLRIllegalStateException("Side does not have points for every turn");
        }
    }

    private static void validateMandatoryGameInfo(Game game) {
        validateDurationNonNegative(game);
        validateTurnLengthNonNegative(game);
        validateSideIsNotNull(game.getFirstGameSide());
        validateSideHasCorrectAmountOfPoints(game, game.getFirstGameSide());
        validateSideHasCorrectAmountOfPoints(game, game.getSecondGameSide());
        if (Boolean.TRUE.equals(game.getGameData().ranking())) {
            validateRankingGameHasFullData(game);
            validateRankingGameHasCorrectPlayers(game);
            validateRankingGameHasNoPlayerOnBothSides(game);
        }
    }

    public static void validateStartingGame(Game game) throws HPLRValidationException {
        try {
            validateMandatoryGameInfo(game);
        } catch (HPLRIllegalStateException | HPLRIllegalArgumentException e) {
            throw new HPLRValidationException(e.toString());
        }
    }

    public static void validateSecondSide(Game game){
        try{
            validateSideHasCorrectAmountOfPoints(game, game.getSecondGameSide());
        }
        catch (HPLRIllegalStateException ex){
            throw new HPLRValidationException(ex.getMessage());
        }
    }

    public static void validateFinishedGame(Game game){
        try{
            validateMandatoryGameInfo(game);
            validateSideHasFilledTurnPoints(game.getFirstGameSide());
            validateSideHasFilledTurnPoints(game.getSecondGameSide());
        }
        catch (HPLRIllegalStateException | HPLRIllegalArgumentException ex){
            throw new HPLRValidationException(ex.getMessage());
        }

    }

    private GameValidator() {
        throw new IllegalArgumentException("Utility class");
    }

}
