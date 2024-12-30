package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.repositories.GameSideRepository;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class GameSideQueryAdapterTests {
    private AutoCloseable closeable;

    static final UUID sideId_wrong = UUID.randomUUID();
    static final UUID sideId_correct = UUID.randomUUID();
    static final UUID test_gameId = UUID.randomUUID();
    static final String test_name = "location";
    static final String test_player_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;

    @Mock
    private GameSideRepository gameSideRepository;

    @InjectMocks
    private GameSideQueryAdapter gameSideQueryAdapter;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void select_side_and_throw_HPLRIllegalStateException() {
        when(gameSideRepository.findBySideId(sideId_wrong))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> gameSideQueryAdapter.selectGameSideBySideId(sideId_wrong));
    }

    @Test
    void select_side_and_succeed() {
        when(gameSideRepository.findBySideId(sideId_correct))
                .thenReturn(Optional.of(new GameSideEntity(
                        null,
                        UUID.randomUUID(),
                        Allegiance.LOYALIST,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_gameId,
                                                test_player_name,
                                                test_email,
                                                test_pwHash,
                                                test_registrationTime,
                                                test_lastLogin,
                                                test_nickname,
                                                test_motto,
                                                test_score
                                        ),
                                        new GameArmyEntity(
                                                null,
                                                new GameArmyTypeEntity(
                                                        null,
                                                        "IH"
                                                ),
                                                test_name,
                                                test_score
                                        ),
                                        null
                                )
                        ),
                        null,
                        List.of(
                                new GameTurnScoreEntity(
                                        null,
                                        1L,
                                        0L,
                                        false
                                )
                        )
                )));
        Assertions.assertDoesNotThrow(
                () -> {
                    GameSideSelectDto gameSideSelectDto = gameSideQueryAdapter.selectGameSideBySideId(sideId_correct);
                    Assertions.assertNotNull(gameSideSelectDto);
                });
    }
}
