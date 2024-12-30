package org.hplr.game.core.usecases.port.in;

import java.util.UUID;

public interface FinishGameUseCaseInterface {
    UUID finishGame(UUID gameId);
}
