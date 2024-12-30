package org.hplr.elo.core.usecases.port.in;

import java.util.List;

public interface CalculateAverageELOForGameSideUseCaseInterface {
    Long calculateAverageELO(List<Long> eloList);
}
