package org.hplr.core.usecases.port.in;

import org.hplr.core.model.GameSnapshot;

import java.util.List;
import java.util.UUID;

public interface GetAllAvailableGamesUseCaseInterface {
    List<GameSnapshot> getAllAvailableGames(UUID playerId);
}
