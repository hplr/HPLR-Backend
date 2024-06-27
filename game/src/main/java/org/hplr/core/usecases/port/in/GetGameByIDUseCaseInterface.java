package org.hplr.core.usecases.port.in;

import org.hplr.core.model.GameSnapshot;

import java.util.UUID;

public interface GetGameByIDUseCaseInterface {
    GameSnapshot getGameByID(UUID gameId);
}
