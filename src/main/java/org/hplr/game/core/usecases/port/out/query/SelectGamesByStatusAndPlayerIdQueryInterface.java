package org.hplr.game.core.usecases.port.out.query;

import org.hplr.game.core.enums.Status;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;

import java.util.List;
import java.util.UUID;

public interface SelectGamesByStatusAndPlayerIdQueryInterface {
    List<GameSelectDto> selectGamesByStatusAndPlayerId(Status status, UUID playerId);
}
