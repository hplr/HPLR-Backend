package org.hplr.user.infrastructure.dbadapter.adapters;

import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class PlayerQueryAdapterTests {

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
    private PlayerQueryRepository playerQueryRepository;

    @InjectMocks
    private PlayerQueryAdapter playerQueryAdapter;


    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void get_nonexistent_player_and_throw_NoSuchElementException() {
        UUID failedUserId = UUID.randomUUID();
        when(playerQueryRepository.findByUserId(failedUserId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> playerQueryAdapter.selectPlayerByUserId(failedUserId));
        verify(playerQueryRepository,atLeastOnce()).findByUserId(any());
    }

    @Test
    void get_existent_player_and_succeed() {
        PlayerEntity player = new PlayerEntity(
                test_playerId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        when(playerQueryRepository.findByUserId(test_playerId))
                .thenReturn(Optional.of(player));
        Optional<PlayerSelectDto> playerSelectDto =  Assertions.assertDoesNotThrow(
                () -> playerQueryAdapter.selectPlayerByUserId(test_playerId));
        verify(playerQueryRepository,atLeastOnce()).findByUserId(any());
        Assertions.assertNotNull(playerSelectDto);
        Assertions.assertTrue(playerSelectDto.isPresent());
    }

    @Test
    void get_all_existent_players_and_succeed() {
        PlayerEntity player = new PlayerEntity(
                test_playerId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        when(playerQueryRepository.findAll())
                .thenReturn(List.of(player));
        List<PlayerSelectDto> playerSelectDtoList =  Assertions.assertDoesNotThrow(
                () -> playerQueryAdapter.selectAllPlayerList());
        verify(playerQueryRepository,atLeastOnce()).findAll();
        Assertions.assertNotNull(playerSelectDtoList);
        Assertions.assertEquals(1, playerSelectDtoList.size());
    }

    @Test
    void get_all_existent_players_from_given_list_and_succeed() {
        PlayerEntity player = new PlayerEntity(
                test_playerId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        when(playerQueryRepository.findAllByUserIdIn(List.of(test_playerId)))
                .thenReturn(List.of(player));
        List<PlayerSelectDto> playerSelectDtoList =  Assertions.assertDoesNotThrow(
                () -> playerQueryAdapter.selectAllPlayerByIdList(List.of(test_playerId)));
        verify(playerQueryRepository,atLeastOnce()).findAllByUserIdIn(List.of(test_playerId));
        Assertions.assertNotNull(playerSelectDtoList);
        Assertions.assertEquals(1, playerSelectDtoList.size());
    }


}
