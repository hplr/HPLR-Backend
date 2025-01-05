package org.hplr.game.core.usecases.port.in;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface FinishGameUseCaseInterface {
    UUID finishGame(HttpServletRequest httpServletRequest, UUID gameId);
}
