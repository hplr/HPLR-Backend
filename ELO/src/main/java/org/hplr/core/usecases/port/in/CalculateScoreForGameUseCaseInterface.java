package org.hplr.core.usecases.port.in;

import java.util.List;

public interface CalculateScoreForGameUseCaseInterface {
    Long calculateScoreForGame(List<Long> firstSideScore, List<Long> secondSideScore);
}
