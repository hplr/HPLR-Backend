package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.usecases.port.dto.SaveScoreForGameSideDto;

import java.util.UUID;

public interface SaveScoreForGameSideUseCaseInterface {
    UUID saveScoreForGameSide(SaveScoreForGameSideDto saveScoreForGameSideDto);
}
