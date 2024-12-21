package org.hplr.elo.core.usecases.port.in;

import java.util.Map;

public interface CalculateELOChangeForGameUseCaseInterface {

    Map<Integer, Long> calculateChangeForGame(Long firstElo, Long secondElo, Long gameScore);
}
