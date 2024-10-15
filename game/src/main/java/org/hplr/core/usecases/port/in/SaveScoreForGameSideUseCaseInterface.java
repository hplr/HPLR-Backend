package org.hplr.core.usecases.port.in;

import org.hplr.core.usecases.port.dto.SaveScoreForGameSideDto;

import java.util.UUID;

public interface SaveScoreForGameSideUseCaseInterface {
    UUID saveScoreForGameSide(SaveScoreForGameSideDto saveScoreForGameSideDto);
}
