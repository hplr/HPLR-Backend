package org.hplr.core.usecases.port.in;

import org.hplr.core.model.vo.GameMission;

import java.util.List;

public interface GetAllGameMissionsUseCaseInterface {
    List<GameMission> getAllGameMissions();
}
