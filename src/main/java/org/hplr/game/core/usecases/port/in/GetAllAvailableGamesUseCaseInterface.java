package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.model.GameSnapshot;

import java.util.List;
import java.util.UUID;

public interface GetAllAvailableGamesUseCaseInterface {
    List<GameSnapshot> getAllAvailableGames(UUID playerId);
}
