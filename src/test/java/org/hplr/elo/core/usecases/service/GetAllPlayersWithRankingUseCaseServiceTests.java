package org.hplr.elo.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.elo.core.usecases.port.dto.PlayerRankingDto;
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

@RequiredArgsConstructor
 class GetAllPlayersWithRankingUseCaseServiceTests {

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
    static final Long test_score_next = 26L;
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
            test_score_next,
            test_pwHash,
            test_registrationTime,
            test_lastLogin
    );

    @Mock
    private SelectAllPlayerListQueryInterface selectAllPlayerListQueryInterface;

    @InjectMocks
    private GetAllPlayersWithRankingUseCaseService getAllPlayersWithRankingUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_no_players_and_succeed(){
        when(selectAllPlayerListQueryInterface.selectAllPlayerList())
                .thenReturn(List.of());
        Assertions.assertDoesNotThrow(
                () -> {
                    List<PlayerRankingDto> playerRankingDtoList = getAllPlayersWithRankingUseCaseService.getAllPlayersWithRanking();
                    Assertions.assertNotNull(playerRankingDtoList);
                    Assertions.assertTrue(playerRankingDtoList.isEmpty());
                });

    }

    @Test
    void get_two_players_and_succeed(){
        when(selectAllPlayerListQueryInterface.selectAllPlayerList())
                .thenReturn(List.of(test_playerSelectDto, test_playerSelectDto_next));
        Assertions.assertDoesNotThrow(
                () -> {
                    List<PlayerRankingDto> playerRankingDtoList = getAllPlayersWithRankingUseCaseService.getAllPlayersWithRanking();
                    Assertions.assertNotNull(playerRankingDtoList);
                    Assertions.assertFalse(playerRankingDtoList.isEmpty());
                    playerRankingDtoList.forEach(
                            Assertions::assertNotNull
                    );
                    Assertions.assertTrue(playerRankingDtoList.get(0).score()> playerRankingDtoList.get(1).score());
                });

    }
}
