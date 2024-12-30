package org.hplr.game.core.usecases.port.out.query;

import org.hplr.game.core.usecases.port.dto.GameSideSelectDto;

import java.util.UUID;

public interface SelectGameSideBySideIdQueryInterface {
    GameSideSelectDto selectGameSideBySideId(UUID sideId);
}
