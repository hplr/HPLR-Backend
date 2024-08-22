package org.hplr.core.usecases.port.out.query;

import org.hplr.core.model.vo.GameMission;

import java.util.List;

public interface SelectAllGameMissionsQueryInterface {
    List<GameMission> getAllGameMissions();
}
