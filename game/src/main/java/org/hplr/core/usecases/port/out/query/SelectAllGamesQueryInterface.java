package org.hplr.core.usecases.port.out.query;

import org.hplr.core.usecases.port.dto.GameSelectDto;

import java.util.List;

public interface SelectAllGamesQueryInterface {
    List<GameSelectDto> selectAllGames();
}
