package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.hplr.game.infrastructure.dbadapter.repositories.GameArmyTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class GameArmyTypeQueryAdapterTests {

    private AutoCloseable closeable;
    static final GameArmyTypeEntity test_army_first = new GameArmyTypeEntity(null, "test_1");
    static final GameArmyTypeEntity test_army_second = new GameArmyTypeEntity(null, "test_2");

    @Mock
    private GameArmyTypeRepository gameArmyTypeRepository;

    @InjectMocks
    private GameArmyTypeQueryAdapter gameArmyTypeQueryAdapter;

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
        when(gameArmyTypeRepository.findAll()).thenReturn(new ArrayList<>());
        List<GameArmyType> gameArmyList = gameArmyTypeQueryAdapter.getAllGameArmyTypes();
        Assertions.assertNotNull(gameArmyList);
        Assertions.assertTrue(gameArmyList.isEmpty());
        verify(gameArmyTypeRepository, times(1)).findAll();
    }

    @Test
    void fetch_two_armies_and_succeed() {
        when(gameArmyTypeRepository.findAll()).thenReturn(
                List.of(test_army_first, test_army_second)
        );
        List<GameArmyType> gameArmyList = gameArmyTypeQueryAdapter.getAllGameArmyTypes();
        Assertions.assertNotNull(gameArmyList);
        Assertions.assertFalse(gameArmyList.isEmpty());
        Assertions.assertEquals(2, gameArmyList.size());
        gameArmyList.forEach(Assertions::assertNotNull);
        verify(gameArmyTypeRepository, times(1)).findAll();
    }
}
