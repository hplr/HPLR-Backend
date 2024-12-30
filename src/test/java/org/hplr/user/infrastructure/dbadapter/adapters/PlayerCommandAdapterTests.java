package org.hplr.user.infrastructure.dbadapter.adapters;

import org.hplr.user.core.model.PlayerFullDataSnapshot;
import org.hplr.user.core.model.vo.PlayerRanking;
import org.hplr.user.core.model.vo.PlayerSecurity;
import org.hplr.user.core.model.vo.UserData;
import org.hplr.user.core.model.vo.UserId;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerCommandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PlayerCommandAdapterTests {

    private AutoCloseable closeable;

    static final UUID test_playerId = UUID.randomUUID();
    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;

    @Mock
    private PlayerCommandRepository playerCommandRepository;

    @InjectMocks
    private PlayerCommandAdapter playerCommandAdapter;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_player_and_succeed() {
        PlayerFullDataSnapshot test_playerFullDataSnapshot = new PlayerFullDataSnapshot(
                new UserId(test_playerId),
                new UserData(
                        test_name,
                        test_nickname,
                        test_email,
                        test_motto),
                new PlayerRanking(test_score),
                new PlayerSecurity(
                        test_pwHash,
                        test_registrationTime,
                        test_lastLogin
                ));
        Assertions.assertDoesNotThrow(
                () -> playerCommandAdapter.savePlayer(test_playerFullDataSnapshot));
        verify(playerCommandRepository, atLeastOnce()).save(any());

    }

    @Test
    void save_last_login_date_for_player_and_succeed() {
        UUID test_player_uuid = UUID.randomUUID();
        LocalDateTime test_login_date = LocalDateTime.now();
        Assertions.assertDoesNotThrow(
                () -> playerCommandAdapter.saveLastLoginDate(test_login_date, test_player_uuid));
        verify(playerCommandRepository, atLeastOnce()).updateLastLoginDate(test_login_date, test_player_uuid);
    }


}
