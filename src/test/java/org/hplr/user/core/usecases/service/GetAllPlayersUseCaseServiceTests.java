package org.hplr.user.core.usecases.service;

import org.hplr.user.core.model.PlayerSnapshot;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerListQueryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetAllPlayersUseCaseServiceTests {

    private AutoCloseable closeable;

    static final UUID test_playerId = UUID.randomUUID();
    static final UUID test_playerId_next = UUID.randomUUID();
    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;
    static final PlayerSelectDto test_playerSelectDto = new PlayerSelectDto(
            test_playerId,
            test_name,
            test_nickname,
            test_email,
            test_motto,
            test_score,
            test_pwHash,
            test_registrationTime,
            test_lastLogin
    );
    static final PlayerSelectDto test_playerSelectDto_next = new PlayerSelectDto(
            test_playerId_next,
            test_name,
            test_nickname,
            test_email,
            test_motto,
            test_score,
            test_pwHash,
            test_registrationTime,
            test_lastLogin
    );

    static final PlayerSelectDto test_playerSelectDto_wrong = new PlayerSelectDto(
            test_playerId_next,
            test_name,
            test_nickname,
            test_email,
            test_motto,
            null,
            test_pwHash,
            test_registrationTime,
            test_lastLogin
    );

    @Mock
    private SelectAllPlayerListQueryInterface selectAllPlayerListQueryInterface;

    @InjectMocks
    private GetAllPlayerListUseCaseService getAllPlayerListUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_no_players_and_succeed() {
        when(selectAllPlayerListQueryInterface.selectAllPlayerList())
                .thenReturn(List.of());
        Assertions.assertDoesNotThrow(
                () -> {
                    List<PlayerSnapshot> playerSnapshotList = getAllPlayerListUseCaseService.getAllPlayerList();
                    Assertions.assertNotNull(playerSnapshotList);
                    Assertions.assertTrue(playerSnapshotList.isEmpty());
                });

    }


    @Test
    void get_two_players_and_succeed(){
        when(selectAllPlayerListQueryInterface.selectAllPlayerList())
                .thenReturn(List.of(test_playerSelectDto, test_playerSelectDto_next));
        Assertions.assertDoesNotThrow(
                () -> {
                    List<PlayerSnapshot> playerSnapshotList = getAllPlayerListUseCaseService.getAllPlayerList();
                    Assertions.assertNotNull(playerSnapshotList);
                    Assertions.assertFalse(playerSnapshotList.isEmpty());
                    playerSnapshotList.forEach(
                            Assertions::assertNotNull
                    );
                });

    }

    @Test
    void get_two_players_and_return_one_and_succeed(){
        when(selectAllPlayerListQueryInterface.selectAllPlayerList())
                .thenReturn(List.of(test_playerSelectDto, test_playerSelectDto_wrong));
        Assertions.assertDoesNotThrow(
                () -> {
                    List<PlayerSnapshot> playerSnapshotList = getAllPlayerListUseCaseService.getAllPlayerList();
                    Assertions.assertNotNull(playerSnapshotList);
                    Assertions.assertFalse(playerSnapshotList.isEmpty());
                    Assertions.assertEquals(1, playerSnapshotList.size());
                    playerSnapshotList.forEach(
                            Assertions::assertNotNull
                    );
                });

    }


}
