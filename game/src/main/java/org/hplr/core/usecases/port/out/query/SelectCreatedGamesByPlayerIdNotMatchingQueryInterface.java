package org.hplr.core.usecases.port.out.query;

import org.hplr.core.usecases.port.dto.GameSelectDto;

import java.util.List;
import java.util.UUID;

public interface SelectCreatedGamesByPlayerIdNotMatchingQueryInterface {
    List<GameSelectDto> selectCreatedGamesByPlayerIdNotMatching(UUID playerId);
}
