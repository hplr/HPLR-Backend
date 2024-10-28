package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;

import org.hplr.game.core.model.Game;
import org.hplr.game.core.usecases.port.dto.SaveScoreForGameSideDto;
import org.hplr.game.core.usecases.port.in.SaveScoreForGameSideUseCaseInterface;
import org.hplr.game.core.usecases.port.out.command.SaveScoreCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;

import org.hplr.elo.core.model.vo.Score;

import org.hplr.library.exception.HPLRIllegalArgumentException;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaveScoreForGameSideUseCaseService implements SaveScoreForGameSideUseCaseInterface {

    final SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;
    final SaveScoreCommandInterface saveScoreCommandInterface;

    @Override
    public UUID saveScoreForGameSide(SaveScoreForGameSideDto saveScoreForGameSideDto) {
        Game game = selectGameByGameIdQueryInterface.selectGameByGameId(saveScoreForGameSideDto.gameId()).map(Game::fromDto).orElseThrow(()-> new HPLRIllegalArgumentException("Game not found"));
        if(saveScoreForGameSideDto.gameSideId().equals(game.getFirstGameSide().getSideId().sideId())){
            Score score = game.getFirstGameSide().getScorePerTurnList().stream().filter(scoreLambda -> saveScoreForGameSideDto.turnNumber().equals(scoreLambda.turnNumber())).findFirst().orElseThrow(() -> new HPLRIllegalArgumentException("Turn not found"));
            game.getFirstGameSide().getScorePerTurnList().remove(score);
            score = new Score(saveScoreForGameSideDto.turnNumber(),saveScoreForGameSideDto.score(), saveScoreForGameSideDto.table());
            game.getFirstGameSide().getScorePerTurnList().add(score);
            saveScoreCommandInterface.saveScore(game.toSnapshot(), score,true);



        }
        else if(saveScoreForGameSideDto.gameSideId().equals(game.getSecondGameSide().getSideId().sideId())){
            Score score = game.getSecondGameSide().getScorePerTurnList().stream().filter(scoreLambda -> saveScoreForGameSideDto.turnNumber().equals(scoreLambda.turnNumber())).findFirst().orElseThrow(() -> new HPLRIllegalArgumentException("Turn not found"));
            game.getSecondGameSide().getScorePerTurnList().remove(score);
            score = new Score(saveScoreForGameSideDto.turnNumber(),saveScoreForGameSideDto.score(), saveScoreForGameSideDto.table());
            game.getSecondGameSide().getScorePerTurnList().add(score);
            saveScoreCommandInterface.saveScore(game.toSnapshot(), score,false);
        }
        return game.getGameId().gameId();
    }
}
