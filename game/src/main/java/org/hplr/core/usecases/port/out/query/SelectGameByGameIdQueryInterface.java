package org.hplr.core.usecases.port.out.query;

import org.hplr.core.usecases.port.dto.GameSelectDto;

import java.util.Optional;
import java.util.UUID;

public interface SelectGameByGameIdQueryInterface {
    Optional<GameSelectDto> selectGameByGameId(UUID gameId);
}
