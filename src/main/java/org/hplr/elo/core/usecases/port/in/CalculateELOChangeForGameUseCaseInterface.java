package org.hplr.elo.core.usecases.port.in;

public interface CalculateELOChangeForGameUseCaseInterface {

    Long calculateChangeForGame(Long firstElo, Long secondElo, Long gameScore);
}