package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.model.GameSnapshot;

import java.util.UUID;

public interface GetGameByIDUseCaseInterface {
    GameSnapshot getGameByID(UUID gameId);
}
