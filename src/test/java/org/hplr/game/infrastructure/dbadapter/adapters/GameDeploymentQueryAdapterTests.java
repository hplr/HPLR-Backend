package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.infrastructure.dbadapter.entities.GameDeploymentEntity;
import org.hplr.game.infrastructure.dbadapter.repositories.GameDeploymentRepository;
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
class GameDeploymentQueryAdapterTests {
    private AutoCloseable closeable;
    static final GameDeploymentEntity test_deployment_first = new GameDeploymentEntity(null, "test_1");
    static final GameDeploymentEntity test_deployment_second = new GameDeploymentEntity(null, "test_2");

    @Mock
    private GameDeploymentRepository gameDeploymentRepository;

    @InjectMocks
    private GameDeploymentQueryAdapter gameDeploymentQueryAdapter;

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
        when(gameDeploymentRepository.findAll()).thenReturn(new ArrayList<>());
        List<GameDeployment> gameArmyList = gameDeploymentQueryAdapter.getAllGameDeployments();
        Assertions.assertNotNull(gameArmyList);
        Assertions.assertTrue(gameArmyList.isEmpty());
        verify(gameDeploymentRepository, times(1)).findAll();
    }

    @Test
    void fetch_two_armies_and_succeed() {
        when(gameDeploymentRepository.findAll()).thenReturn(
                List.of(test_deployment_first, test_deployment_second)
        );
        List<GameDeployment> gameArmyList = gameDeploymentQueryAdapter.getAllGameDeployments();
        Assertions.assertNotNull(gameArmyList);
        Assertions.assertFalse(gameArmyList.isEmpty());
        Assertions.assertEquals(2, gameArmyList.size());
        gameArmyList.forEach(Assertions::assertNotNull);
        verify(gameDeploymentRepository, times(1)).findAll();
    }
}
