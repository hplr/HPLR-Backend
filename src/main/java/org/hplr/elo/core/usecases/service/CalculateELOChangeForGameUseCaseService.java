package org.hplr.elo.core.usecases.service;

import org.hplr.elo.core.usecases.port.in.CalculateELOChangeForGameUseCaseInterface;
import org.hplr.library.core.util.ConstValues;
import org.springframework.stereotype.Service;

@Service
public class CalculateELOChangeForGameUseCaseService implements CalculateELOChangeForGameUseCaseInterface {


    @Override
    public Long calculateChangeForGame(Long firstElo, Long secondElo, Long gameScore) {
        Double firstPlayerWinProbability =  (1 / (1+Math.pow(10, ((double) (-(firstElo - secondElo)) / (ConstValues.INITIAL_WEIGHT)))));
        return Math.round(firstPlayerWinProbability * gameScore);

    }
}
