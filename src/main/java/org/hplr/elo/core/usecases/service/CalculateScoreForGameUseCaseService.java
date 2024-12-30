package org.hplr.elo.core.usecases.service;

import org.hplr.elo.core.usecases.port.in.CalculateScoreForGameUseCaseInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalculateScoreForGameUseCaseService implements CalculateScoreForGameUseCaseInterface {
    @Override
    public Long calculateScoreForGame(List<Long> firstSideScore, List<Long> secondSideScore) {
        Optional<Long> firstSideScoreSum = firstSideScore
                .stream()
                .reduce(Long::sum);
        Optional<Long> secondSideScoreSum = secondSideScore
                .stream()
                .reduce(Long::sum);
        if(firstSideScoreSum.isPresent() && secondSideScoreSum.isPresent()){
            return firstSideScoreSum.get() - secondSideScoreSum.get();
        }
        return null;
    }
}
