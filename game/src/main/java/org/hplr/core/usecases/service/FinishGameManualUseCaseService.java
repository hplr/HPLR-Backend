package org.hplr.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.core.enums.Status;
import org.hplr.core.model.Game;
import org.hplr.core.model.vo.PlayerRanking;
import org.hplr.core.model.vo.Score;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.in.CalculateAverageELOForGameSideUseCaseInterface;
import org.hplr.core.usecases.port.in.CalculateELOChangeForGameUseCaseInterface;
import org.hplr.core.usecases.port.in.CalculateScoreForGameUseCaseInterface;
import org.hplr.core.usecases.port.in.FinishGameManualUseCaseInterface;
import org.hplr.core.usecases.port.out.command.SaveFinishedGameCommandInterface;
import org.hplr.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FinishGameManualUseCaseService implements FinishGameManualUseCaseInterface {

    final SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;
    final SaveFinishedGameCommandInterface saveFinishedGameCommandInterface;
    final CalculateELOChangeForGameUseCaseInterface calculateELOChangeForGameUseCaseInterface;
    final CalculateAverageELOForGameSideUseCaseInterface calculateAverageELOForGameSideUseCaseInterface;
    final CalculateScoreForGameUseCaseInterface calculateScoreForGameUseCaseInterface;

    @Override
    public UUID finishGameManual(UUID gameId) {
        Optional<GameSelectDto> gameSelectDtoOptional =
                selectGameByGameIdQueryInterface.selectGameByGameId(gameId);

        Game game = Game.fromDto(gameSelectDtoOptional.orElseThrow(NoSuchElementException::new));
        //todo: validate finished game
        //GameValidator.validateFinishedGame(game);
        game.setGameStatus(Status.FINISHED);
        Long firstElo = calculateAverageELOForGameSideUseCaseInterface.calculateAverageELO(
                game.getFirstGameSide().getGameSidePlayerDataList()
                        .stream()
                        .map(gameSidePlayerData -> gameSidePlayerData.currentELO().ELOValue())
                        .toList()
        );
        Long secondElo = calculateAverageELOForGameSideUseCaseInterface.calculateAverageELO(
                game.getSecondGameSide().getGameSidePlayerDataList()
                        .stream()
                        .map(gameSidePlayerData -> gameSidePlayerData.currentELO().ELOValue())
                        .toList()
        );
        Long gameScore;
        if(game.getFirstGameSide().getScorePerTurnList()
                .stream()
                .anyMatch(Score::tabled)
              ){
            gameScore = 20L;
        }
        else if(
                game.getSecondGameSide().getScorePerTurnList()
                        .stream()
                        .anyMatch(Score::tabled)
        ){
            gameScore = -20L;
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

        Long eloChange = calculateELOChangeForGameUseCaseInterface.calculateChangeForGame(firstElo, secondElo, gameScore);
        game.getFirstGameSide().getGameSidePlayerDataList()
                .forEach(gameSidePlayerData -> {
                            Long score = gameSidePlayerData.player().getRanking().score();
                            PlayerRanking playerRanking = new PlayerRanking( score + eloChange);
                            gameSidePlayerData.player().setRanking(playerRanking);
                        }
                );
        game.getSecondGameSide().getGameSidePlayerDataList()
                .forEach(gameSidePlayerData -> {
                            Long score = gameSidePlayerData.player().getRanking().score();
                            PlayerRanking playerRanking = new PlayerRanking( score - eloChange);
                            gameSidePlayerData.player().setRanking(playerRanking);
                        }
                );
        saveFinishedGameCommandInterface.saveFinishedGame(game.toSnapshot());
        return game.getGameId().gameId();
    }
}
