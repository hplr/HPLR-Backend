package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.infrastructure.dbadapter.entities.GameMissionEntity;
import org.hplr.game.infrastructure.dbadapter.repositories.GameMissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class GameMissionQueryAdapterTests {
    private AutoCloseable closeable;
    static final GameMissionEntity test_mission_first = new GameMissionEntity(null, "test_1");
    static final GameMissionEntity test_mission_second = new GameMissionEntity(null, "test_2");

    @Mock
    private GameMissionRepository gameMissionRepository;

    @InjectMocks
    private GameMissionQueryAdapter gameMissionQueryAdapter;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void fetch_no_armies_and_return_empty_collection() {
        when(gameMissionRepository.findAll()).thenReturn(new ArrayList<>());
        List<GameMission> gameArmyList = gameMissionQueryAdapter.getAllGameMissions();
        Assertions.assertNotNull(gameArmyList);
        Assertions.assertTrue(gameArmyList.isEmpty());
        verify(gameMissionRepository, times(1)).findAll();
    }

    @Test
    void fetch_two_armies_and_succeed() {
        when(gameMissionRepository.findAll()).thenReturn(
                List.of(test_mission_first, test_mission_second)
        );
        List<GameMission> gameArmyList = gameMissionQueryAdapter.getAllGameMissions();
        Assertions.assertNotNull(gameArmyList);
        Assertions.assertFalse(gameArmyList.isEmpty());
        Assertions.assertEquals(2, gameArmyList.size());
        gameArmyList.forEach(Assertions::assertNotNull);
        verify(gameMissionRepository, times(1)).findAll();
    }
}
