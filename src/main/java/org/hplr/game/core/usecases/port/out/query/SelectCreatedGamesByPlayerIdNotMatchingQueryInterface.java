package org.hplr.game.core.usecases.port.out.query;

import org.hplr.game.core.usecases.port.dto.GameSelectDto;

import java.util.List;
import java.util.UUID;

public interface SelectCreatedGamesByPlayerIdNotMatchingQueryInterface {
    List<GameSelectDto> selectCreatedGamesByPlayerIdNotMatching(UUID playerId);
}
