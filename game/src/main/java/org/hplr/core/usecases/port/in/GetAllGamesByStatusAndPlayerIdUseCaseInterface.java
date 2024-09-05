package org.hplr.core.usecases.port.in;

import org.hplr.core.enums.Status;
import org.hplr.core.model.GameSnapshot;

import java.util.List;
import java.util.UUID;

public interface GetAllGamesByStatusAndPlayerIdUseCaseInterface {
    List<GameSnapshot> getAllGamesByStatusAndPlayerId(Status status, UUID playerId);
}
