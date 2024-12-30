package org.hplr.game.core.usecases.port.out.query;

import org.hplr.game.core.model.vo.GameMission;

import java.util.List;

public interface SelectAllGameMissionsQueryInterface {
    List<GameMission> getAllGameMissions();
}
