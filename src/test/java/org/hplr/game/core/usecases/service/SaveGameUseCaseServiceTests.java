package org.hplr.game.core.usecases.service;

import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSaveSideDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerArmyDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.game.core.usecases.port.out.command.SaveGameCommandInterface;

import org.hplr.library.exception.HPLRValidationException;
import org.hplr.library.exception.LocationCalculationException;

import org.hplr.location.core.usecases.port.dto.LocationSaveDto;

import org.hplr.user.core.model.PlayerValidator;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerByIdListQueryInterface;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.mappers.PlayerMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SaveGameUseCaseServiceTests {

    private AutoCloseable closeable;
    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;

    @Mock
    SelectPlayerByUserIdQueryInterface mock_selectPlayerByUserIdQueryInterface;
    @Mock
    SelectAllPlayerByIdListQueryInterface mock_selectAllPlayerByIdListQueryInterface;

    @Mock
    SaveGameCommandInterface mock_saveGameCommandInterface;

    @InjectMocks
    private SaveGameUseCaseService saveGameUseCaseService;

    @Captor
    private ArgumentCaptor<GameSnapshot> captor_GameSnapshot;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_game_and_succeed() {
        UUID test_firstPlayerId = UUID.randomUUID();
        UUID test_secondPlayerId = UUID.randomUUID();
        PlayerEntity first_player = new PlayerEntity(
                test_firstPlayerId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        PlayerEntity second_player = new PlayerEntity(
                test_secondPlayerId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score
        );

        when(mock_selectAllPlayerByIdListQueryInterface.selectAllPlayerByIdList(List.of(test_firstPlayerId)))
                .thenReturn(List.of(PlayerMapper.toDto(first_player)));
        when(mock_selectAllPlayerByIdListQueryInterface.selectAllPlayerByIdList(List.of(test_secondPlayerId)))
                .thenReturn(List.of(PlayerMapper.toDto(second_player)));
        when(mock_selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_firstPlayerId))
                .thenReturn(Optional.of(PlayerMapper.toDto(first_player)));
        when(mock_selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_secondPlayerId))
                .thenReturn(Optional.of(PlayerMapper.toDto(second_player)));
        InitialGameSaveDataDto test_initialGameSaveDataDto = new InitialGameSaveDataDto(
                new InitialGameSaveSideDto(
                        Allegiance.LOYALIST,
                        List.of(new InitialGameSidePlayerDataDto(
                                test_firstPlayerId,
                                new InitialGameSidePlayerArmyDto(
                                        "IH",
                                        "TEST",
                                        1250L
                                ),
                                new ArrayList<>()
                        ))
                ),
                new InitialGameSaveSideDto(
                        Allegiance.TRAITOR,
                        List.of(new InitialGameSidePlayerDataDto(
                                test_secondPlayerId,
                                new InitialGameSidePlayerArmyDto(
                                        "IH",
                                        "TEST",
                                        1250L
                                ),
                                new ArrayList<>()
                        ))
                ),
                false,
                1250L,
                6,
                3,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
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
                () -> saveGameUseCaseService.saveGame(test_initialGameSaveDataDto)
        );
        Assertions.assertNotNull(gameId);
        verify(mock_saveGameCommandInterface).saveGame(captor_GameSnapshot.capture());
        Assertions.assertNotNull(captor_GameSnapshot.getValue().secondGameSide().gameSidePlayerDataList());
        verify(mock_saveGameCommandInterface, times(1)).saveGame(any());
    }

    @Test
    void save_game_and_throw_HPLRValidationException() {
        UUID test_firstPlayerId = UUID.randomUUID();
        UUID test_secondPlayerId = UUID.randomUUID();
        PlayerEntity first_player = new PlayerEntity(
                test_firstPlayerId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        PlayerEntity second_player = new PlayerEntity(
                test_secondPlayerId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score
        );

        when(mock_selectAllPlayerByIdListQueryInterface.selectAllPlayerByIdList(List.of(test_firstPlayerId)))
                .thenReturn(List.of(PlayerMapper.toDto(first_player)));
        when(mock_selectAllPlayerByIdListQueryInterface.selectAllPlayerByIdList(List.of(test_secondPlayerId)))
                .thenReturn(List.of(PlayerMapper.toDto(second_player)));
        InitialGameSaveDataDto test_initialGameSaveDataDto = new InitialGameSaveDataDto(
                new InitialGameSaveSideDto(
                        Allegiance.LOYALIST,
                        List.of(new InitialGameSidePlayerDataDto(
                                test_firstPlayerId,
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
                                test_secondPlayerId,
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
                0,
                3,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
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
        Assertions.assertThrows(HPLRValidationException.class,
                () -> saveGameUseCaseService.saveGame(test_initialGameSaveDataDto)
        );

        verify(mock_saveGameCommandInterface, times(0)).saveGame(any());
    }

    @Test
    void save_game_and_throw_LocationCalculationException() {
        try (MockedStatic<Game> utilities = Mockito.mockStatic(Game.class)) {
            utilities.when(() -> Game.fromDto(any(), any(), any())).thenThrow(LocationCalculationException.class);
            InitialGameSaveDataDto test_initialGameSaveDataDto = new InitialGameSaveDataDto(
                    new InitialGameSaveSideDto(
                            Allegiance.LOYALIST,
                            List.of(new InitialGameSidePlayerDataDto(
                                    null,
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
                                    null,
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
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
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
            Assertions.assertThrows(LocationCalculationException.class,
                    () -> saveGameUseCaseService.saveGame(test_initialGameSaveDataDto)
            );

            verify(mock_saveGameCommandInterface, times(0)).saveGame(any());

        }
    }

    @Test
    void save_game_and_throw_HPLRIllegalStateException() {
        try (MockedStatic<PlayerValidator> utilities = Mockito.mockStatic(PlayerValidator.class)) {
            utilities.when(() -> PlayerValidator.validatePlayer(any())).thenThrow(HPLRValidationException.class);
            UUID test_firstPlayerId = UUID.randomUUID();
            UUID test_secondPlayerId = UUID.randomUUID();
            PlayerEntity first_player = new PlayerEntity(
                    test_firstPlayerId,
                    test_name,
                    test_email,
                    test_pwHash,
                    test_registrationTime,
                    test_lastLogin,
                    test_nickname,
                    test_motto,
                    test_score);
            PlayerEntity second_player = new PlayerEntity(
                    test_secondPlayerId,
                    test_name,
                    test_email,
                    test_pwHash,
                    test_registrationTime,
                    test_lastLogin,
                    test_nickname,
                    test_motto,
                    test_score
            );

            when(mock_selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_firstPlayerId))
                    .thenReturn(Optional.of(PlayerMapper.toDto(first_player)));
            when(mock_selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_secondPlayerId))
                    .thenReturn(Optional.of(PlayerMapper.toDto(second_player)));
            InitialGameSaveDataDto test_initialGameSaveDataDto = new InitialGameSaveDataDto(
                    new InitialGameSaveSideDto(
                            Allegiance.LOYALIST,
                            List.of(new InitialGameSidePlayerDataDto(
                                    test_firstPlayerId,
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
                                    test_secondPlayerId,
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
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
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
            Assertions.assertThrows(HPLRValidationException.class,
                    () -> saveGameUseCaseService.saveGame(test_initialGameSaveDataDto)
            );

            verify(mock_saveGameCommandInterface, times(0)).saveGame(any());
        }
    }

}
