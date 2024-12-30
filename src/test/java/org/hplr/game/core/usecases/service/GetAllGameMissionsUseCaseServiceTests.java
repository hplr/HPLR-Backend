package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameMissionsQueryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class GetAllGameMissionsUseCaseServiceTests {
    private AutoCloseable closeable;

    @Mock
    private SelectAllGameMissionsQueryInterface selectAllGameMissionsQueryInterface;

    @InjectMocks
    private GetAllGameMissionsUseCaseService getAllGameMissionsUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void select_no_game_missions_and_succeed(){
        when(selectAllGameMissionsQueryInterface.getAllGameMissions())
                .thenReturn(new ArrayList<>());
        Assertions.assertTrue(getAllGameMissionsUseCaseService.getAllGameMissions().isEmpty());
    }

    @Test
    void select_existing_game_missions_and_succeed(){
        when(selectAllGameMissionsQueryInterface.getAllGameMissions())
                .thenReturn(List.of(new GameMission(
                        "test"
                )));
        List<GameMission> result = getAllGameMissionsUseCaseService.getAllGameMissions();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        result.forEach(Assertions::assertNotNull);
    }

}
