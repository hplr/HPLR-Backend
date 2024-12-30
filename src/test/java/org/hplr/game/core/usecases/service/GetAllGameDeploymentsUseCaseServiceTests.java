package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameDeploymentsQueryInterface;
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
class GetAllGameDeploymentsUseCaseServiceTests {
    private AutoCloseable closeable;


    @Mock
    private SelectAllGameDeploymentsQueryInterface selectAllGameDeploymentsQueryInterface;

    @InjectMocks
    private GetAllGameDeploymentsUseCaseService getAllGameDeploymentsUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void select_no_game_deployments_and_succeed(){
        when(selectAllGameDeploymentsQueryInterface.getAllGameDeployments())
                .thenReturn(new ArrayList<>());
        Assertions.assertTrue(getAllGameDeploymentsUseCaseService.getAllGameDeployments().isEmpty());
    }

    @Test
    void select_existing_game_deployments_and_succeed(){
        when(selectAllGameDeploymentsQueryInterface.getAllGameDeployments())
                .thenReturn(List.of(new GameDeployment(
                        "test"
                )));
        List<GameDeployment> result = getAllGameDeploymentsUseCaseService.getAllGameDeployments();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        result.forEach(Assertions::assertNotNull);
    }

}
