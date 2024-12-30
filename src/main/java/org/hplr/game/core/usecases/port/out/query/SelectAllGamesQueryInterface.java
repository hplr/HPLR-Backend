package org.hplr.game.core.usecases.port.out.query;

import org.hplr.game.core.usecases.port.dto.GameSelectDto;

import java.util.List;

public interface SelectAllGamesQueryInterface {
    List<GameSelectDto> selectAllGames();
}
