package org.hplr.elo.core.usecases.service;

import org.hplr.elo.core.usecases.port.in.CalculateELOChangeForGameUseCaseInterface;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateELOChangeForGameUseCaseService implements CalculateELOChangeForGameUseCaseInterface {


    @Override
    public Map<Integer, Long> calculateChangeForGame(Long firstElo, Long secondElo, Long gameScore) {
        double firstPlayerWinProbability = (1 / (1 + Math.pow(10, ((double) (secondElo - firstElo) / (ConstValues.INITIAL_WEIGHT)))));
        double secondPlayerWinProbability = (1 / (1 + Math.pow(10, ((double) (firstElo - secondElo) / (ConstValues.INITIAL_WEIGHT)))));
        Map<Integer, Long> eloChangeForGame = new HashMap<>();
        if (gameScore == 0) {
            eloChangeForGame.put(1, (long) (ConstValues.ELO_CONST * gameScore * (0.5 - firstPlayerWinProbability)));
            eloChangeForGame.put(2, (long) (ConstValues.ELO_CONST * gameScore * (0.5 - secondPlayerWinProbability)));
            return eloChangeForGame;
        }
        if (gameScore >= -20 && gameScore < 0) {
            gameScore = -gameScore;
            eloChangeForGame.put(1, (long) (ConstValues.ELO_CONST * gameScore * (0 - firstPlayerWinProbability)));
            eloChangeForGame.put(2, (long) (ConstValues.ELO_CONST * gameScore * (1 - secondPlayerWinProbability)));
            return eloChangeForGame;
        } else if (gameScore > 0 && gameScore <= 20) {
            eloChangeForGame.put(1, (long) (ConstValues.ELO_CONST * gameScore * (1 - firstPlayerWinProbability)));
            eloChangeForGame.put(2, (long) (ConstValues.ELO_CONST * gameScore * (0 - secondPlayerWinProbability)));
            return eloChangeForGame;
        } else {
            throw new HPLRIllegalStateException("Wrong score!");
        }
    }
}
