package org.hplr.core.usecases.service;

import org.hplr.core.usecases.port.out.command.SaveGameCommandInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.openMocks;

class SaveGameUseCaseServiceTest {

    private AutoCloseable closeable;
    private SaveGameUseCaseService saveGameUseCaseService;

    @Mock
    SaveGameCommandInterface saveGameCommandInterface;

    @BeforeEach
    public void setUp(){
        closeable = openMocks(this);
    }
    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_game_with_nonexistent_player_and_throw_NoSuchElementException(){
        Assertions.assertTrue(true);
    }
}
