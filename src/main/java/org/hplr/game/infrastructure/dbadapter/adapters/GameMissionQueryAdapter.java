package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameMissionsQueryInterface;
import org.hplr.game.infrastructure.dbadapter.repositories.GameMissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameMissionQueryAdapter implements SelectAllGameMissionsQueryInterface {
    final GameMissionRepository gameMissionRepository;


    @Override
    public List<GameMission> getAllGameMissions() {
        return gameMissionRepository
                .findAll()
                .stream()
                .map(gameMission -> new GameMission(
                        gameMission.getName()
                )).toList();
    }
}
