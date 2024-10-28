package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.usecases.port.in.GetAllGameMissionsUseCaseInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameMissionsQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllGameMissionsUseCaseService implements GetAllGameMissionsUseCaseInterface {

    final SelectAllGameMissionsQueryInterface selectAllGameMissionsQueryInterface;

    @Override
    public List<GameMission> getAllGameMissions() {
        return selectAllGameMissionsQueryInterface.getAllGameMissions();
    }
}
