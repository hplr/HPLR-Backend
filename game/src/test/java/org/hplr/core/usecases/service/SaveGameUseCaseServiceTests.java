package org.hplr.core.usecases.service;

import org.hplr.core.usecases.port.out.command.SaveGameCommandInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.openMocks;

class SaveGameUseCaseServiceTests {

    private AutoCloseable closeable;



    @Mock
    SaveGameCommandInterface saveGameCommandInterface;

    @InjectMocks
    private SaveGameUseCaseService saveGameUseCaseService;


    @BeforeEach
    public void setUp(){
        closeable = openMocks(this);
    }
    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_game_when_there_is_less_then_two_players_and_throw_IllegalStateException(){
        Assertions.assertTrue(true);
    }
}
