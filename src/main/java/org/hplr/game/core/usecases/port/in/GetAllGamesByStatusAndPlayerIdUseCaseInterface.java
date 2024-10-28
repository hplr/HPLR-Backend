package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSnapshot;

import java.util.List;
import java.util.UUID;

public interface GetAllGamesByStatusAndPlayerIdUseCaseInterface {
    List<GameSnapshot> getAllGamesByStatusAndPlayerId(Status status, UUID playerId);
}
