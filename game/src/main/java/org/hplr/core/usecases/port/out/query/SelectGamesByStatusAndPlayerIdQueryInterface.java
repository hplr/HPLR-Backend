package org.hplr.core.usecases.port.out.query;

import org.hplr.core.enums.Status;
import org.hplr.core.usecases.port.dto.GameSelectDto;

import java.util.List;
import java.util.UUID;

public interface SelectGamesByStatusAndPlayerIdQueryInterface {
    List<GameSelectDto> selectGamesByStatusAndPlayerId(Status status, UUID playerId);
}
