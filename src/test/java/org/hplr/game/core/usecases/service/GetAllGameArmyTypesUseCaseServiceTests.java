package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameArmyTypesQueryInterface;
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
class GetAllGameArmyTypesUseCaseServiceTests {
    private AutoCloseable closeable;


    @Mock
    private SelectAllGameArmyTypesQueryInterface selectAllGameArmyTypesQueryInterface;

    @InjectMocks
    private GetAllGameArmyTypesUseCaseService getAllGameArmyTypesUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void select_no_game_army_types_and_succeed(){
        when(selectAllGameArmyTypesQueryInterface.getAllGameArmyTypes())
                .thenReturn(new ArrayList<>());
        Assertions.assertTrue(getAllGameArmyTypesUseCaseService.getAllGameArmyTypes().isEmpty());
    }

    @Test
    void select_existing_game_army_types_and_succeed(){
        when(selectAllGameArmyTypesQueryInterface.getAllGameArmyTypes())
                .thenReturn(List.of(new GameArmyType(
                        "test"
                )));
        List<GameArmyType> result = getAllGameArmyTypesUseCaseService.getAllGameArmyTypes();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        result.forEach(Assertions::assertNotNull);
    }

}
