package org.hplr.game.core.usecases.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.hplr.elo.core.model.vo.Elo;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameValidator;
import org.hplr.game.core.model.vo.GameHistoricalElo;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.in.FinishGameUseCaseInterface;
import org.hplr.game.core.usecases.port.out.command.SaveFinishedGameCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;

import org.hplr.library.exception.HPLRAccessDeniedException;
import org.hplr.library.infrastructure.controller.AccessValidator;
import org.hplr.user.core.model.vo.PlayerRanking;

import org.hplr.elo.core.model.vo.Score;
import org.hplr.elo.core.usecases.port.in.CalculateAverageELOForGameSideUseCaseInterface;
import org.hplr.elo.core.usecases.port.in.CalculateELOChangeForGameUseCaseInterface;
import org.hplr.elo.core.usecases.port.in.CalculateScoreForGameUseCaseInterface;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
public class FinishGameManualUseCaseService implements FinishGameUseCaseInterface {

    final SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;
    final SaveFinishedGameCommandInterface saveFinishedGameCommandInterface;
    final CalculateELOChangeForGameUseCaseInterface calculateELOChangeForGameUseCaseInterface;
    final CalculateAverageELOForGameSideUseCaseInterface calculateAverageELOForGameSideUseCaseInterface;
    final CalculateScoreForGameUseCaseInterface calculateScoreForGameUseCaseInterface;
    final AccessValidator accessValidator;

    @Override
    public UUID finishGame(HttpServletRequest httpServletRequest, UUID gameId) {
        AtomicReference<Boolean> firstSidePresent = new AtomicReference<>(false);
        AtomicReference<Boolean> secondSidePresent = new AtomicReference<>(false);
        Optional<GameSelectDto> gameSelectDtoOptional =
                selectGameByGameIdQueryInterface.selectGameByGameId(gameId);

        Game game = Game.fromDto(gameSelectDtoOptional.orElseThrow(NoSuchElementException::new));
        game.getFirstGameSide().getGameSidePlayerDataList().forEach(player->{
            if(Boolean.TRUE.equals(accessValidator.validateUserAccess(httpServletRequest,player.player().getUserId().id()))){
                firstSidePresent.set(true);
            }
        });
        game.getSecondGameSide().getGameSidePlayerDataList().forEach(player->{
            if(Boolean.TRUE.equals(accessValidator.validateUserAccess(httpServletRequest,player.player().getUserId().id()))){
                secondSidePresent.set(true);
            }
        });

        if(!(firstSidePresent.get() || secondSidePresent.get())){
            throw new HPLRAccessDeniedException("Player cannot end this game!");
        }

        GameValidator.validateFinishedGame(game);
        game.setGameStatus(Status.FINISHED);

        Long firstElo = calculateAverageELOForGameSideUseCaseInterface.calculateAverageELO(
                game.getFirstGameSide().getGameSidePlayerDataList()
                        .stream()
                        .map(gameSidePlayerData -> gameSidePlayerData.player().getRanking().score())
                        .toList()
        );
        Long secondElo = calculateAverageELOForGameSideUseCaseInterface.calculateAverageELO(
                game.getSecondGameSide().getGameSidePlayerDataList()
                        .stream()
                        .map(gameSidePlayerData -> gameSidePlayerData.player().getRanking().score())
                        .toList()
        );
        Long gameScore;
        if(game.getFirstGameSide()
                .getScorePerTurnList()
                .stream()
                .anyMatch(Score::tabled)
              ){
            gameScore = -20L;
        }
        else if(
                game.getSecondGameSide().getScorePerTurnList()
                        .stream()
                        .anyMatch(Score::tabled)
        ){
            gameScore = 20L;
        }
        else {
            gameScore = calculateScoreForGameUseCaseInterface.calculateScoreForGame(
                    game.getFirstGameSide().getScorePerTurnList()
                            .stream()
                            .map(Score::turnScore)
                            .toList(),
                    game.getSecondGameSide().getScorePerTurnList()
                            .stream()
                            .map(Score::turnScore)
                            .toList()
            );
        }

        Map<Integer, Long> eloMap = calculateELOChangeForGameUseCaseInterface.calculateChangeForGame(firstElo, secondElo, gameScore);
        game.getFirstGameSide().getGameSidePlayerDataList()
                .forEach(gameSidePlayerData -> {
                            Long score = gameSidePlayerData.player().getRanking().score();
                            PlayerRanking playerRanking = new PlayerRanking( score + eloMap.get(1));
                            gameSidePlayerData.player().setRanking(playerRanking);
                        }
                );
        game.getSecondGameSide().getGameSidePlayerDataList()
                .forEach(gameSidePlayerData -> {
                            Long score = gameSidePlayerData.player().getRanking().score();
                            PlayerRanking playerRanking = new PlayerRanking(score +  eloMap.get(2));
                            gameSidePlayerData.player().setRanking(playerRanking);
                        }
                );
        GameHistoricalElo gameHistoricalElo = new GameHistoricalElo(
                game.getGameId(),
                new Elo(firstElo),
                new Elo(secondElo)
        );
        saveFinishedGameCommandInterface.saveFinishedGame(game.toSnapshot(), gameHistoricalElo);
        return game.getGameId().gameId();
    }
}
