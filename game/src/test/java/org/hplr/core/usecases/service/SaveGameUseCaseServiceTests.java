package org.hplr.core.usecases.service;

import lombok.SneakyThrows;
import org.hplr.core.enums.Allegiance;
import org.hplr.core.usecases.port.dto.*;
import org.hplr.core.usecases.port.out.command.SaveGameCommandInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class SaveGameUseCaseServiceTests {

    private AutoCloseable closeable;


//todo:naming convention
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
    void save_game_and_succeed(){
        InitialGameSaveDataDto test_initialGameSaveDataDto= new InitialGameSaveDataDto(
                new InitialGameSaveSideDto(
                        Allegiance.LOYALIST,
                        List.of(new InitialGameSidePlayerDataDto(
                                UUID.randomUUID(),
                                new InitialGameSidePlayerArmyDto(
                                        "IH",
                                        "TEST",
                                        1250L
                                ),
                                null
                        ))
                ),
                new InitialGameSaveSideDto(
                        Allegiance.TRAITOR,
                        List.of(new InitialGameSidePlayerDataDto(
                                UUID.randomUUID(),
                                new InitialGameSidePlayerArmyDto(
                                        "IH",
                                        "TEST",
                                        1250L
                                ),
                                null
                        ))
                ),
                false,
                1250L,
                6,
                3,
                LocalDateTime.now(),
                new LocationSaveDto(
                        "test",
                        "test",
                        "test",
                        "test",
                        "1",
                        false

                ),
                "TEST",
                "TEST"
        );
        UUID gameId = Assertions.assertDoesNotThrow(
                ()->saveGameUseCaseService.saveGame(test_initialGameSaveDataDto)
        );
        Assertions.assertNotNull(gameId);
        verify(saveGameCommandInterface,times(1)).saveGame(any());
    }
}
