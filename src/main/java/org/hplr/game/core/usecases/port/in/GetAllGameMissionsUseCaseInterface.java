package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.model.vo.GameMission;

import java.util.List;

public interface GetAllGameMissionsUseCaseInterface {
    List<GameMission> getAllGameMissions();
}
