package org.hplr.core.usecases.service;

import org.hplr.core.model.PlayerSnapshot;
import org.hplr.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.exception.HPLRValidationException;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.infrastructure.dbadapter.mappers.PlayerMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetPlayerUseCaseServiceTests {

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
    private SelectPlayerByUserIdQueryInterface selectPlayerByUserIdQueryInterface;

    @InjectMocks
    private GetPlayerUseCaseService getPlayerUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_nonexistent_player_and_throw_NoSuchElementException() throws HPLRValidationException {
        UUID failedUserId = UUID.randomUUID();
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(failedUserId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> getPlayerUseCaseService.getPlayerByUserId(failedUserId));

    }


    @ParameterizedTest
    @MethodSource("incorrectPlayerEntityGenerator")
    void get_existent_invalid_player_and_throw_HPLRValidationException(PlayerEntity playerEntity) throws HPLRValidationException {
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId))
                .thenReturn(Optional.of(PlayerMapper.toDto(playerEntity)));
        UUID userId = playerEntity.getUserId();
        Assertions.assertThrows(HPLRValidationException.class,
                () -> getPlayerUseCaseService.getPlayerByUserId(userId)
        );
    }

    @Test
    void get_existent_valid_player_and_succeed() throws HPLRValidationException {
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
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId))
                .thenReturn(Optional.of(PlayerMapper.toDto(player)));
        PlayerSnapshot playerSnapshot = Assertions.assertDoesNotThrow(
                () -> getPlayerUseCaseService.getPlayerByUserId(test_playerId)
        );
        Assertions.assertNotNull(playerSnapshot);
    }


    private static Stream<Arguments> incorrectPlayerEntityGenerator() {

        return Stream.of(
                Arguments.of(new PlayerEntity(
                        test_playerId,
                        null,
                        test_email,
                        test_pwHash,
                        test_registrationTime,
                        test_lastLogin,
                        test_nickname,
                        test_motto,
                        test_score
                )),
                Arguments.of(new PlayerEntity(
                        test_playerId,
                        test_name,
                        null,
                        test_pwHash,
                        test_registrationTime,
                        test_lastLogin,
                        test_nickname,
                        test_motto,
                        test_score
                )),
                Arguments.of(new PlayerEntity(
                        test_playerId,
                        test_name,
                        test_email,
                        null,
                        test_registrationTime,
                        test_lastLogin,
                        test_nickname,
                        test_motto,
                        test_score
                )),
                Arguments.of(new PlayerEntity(
                        test_playerId,
                        test_name,
                        test_email,
                        test_pwHash,
                        null,
                        test_lastLogin,
                        test_nickname,
                        test_motto,
                        test_score
                )),
                Arguments.of(new PlayerEntity(
                        test_playerId,
                        test_name,
                        test_email,
                        test_pwHash,
                        test_registrationTime,
                        test_lastLogin,
                        null,
                        test_motto,
                        test_score
                )),
                Arguments.of(new PlayerEntity(
                        test_playerId,
                        test_name,
                        test_email,
                        test_pwHash,
                        test_registrationTime,
                        test_lastLogin,
                        test_nickname,
                        test_motto,
                        null
                ))
        );
    }


}
