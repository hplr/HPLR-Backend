package org.hplr.user.core.usecases.service;

import lombok.extern.slf4j.Slf4j;
import org.hplr.library.core.model.StringValidator;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.user.core.usecases.port.dto.GetTokenResponseDto;
import org.hplr.user.core.usecases.port.dto.PlayerLoginDto;
import org.hplr.user.core.usecases.port.out.command.SaveLastLoginDateCommandInterface;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByEmailQueryInterface;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.mappers.PlayerMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@Slf4j
class LoginPlayerUseCaseServiceTests {

    private AutoCloseable closeable;

    static final UUID test_playerId = UUID.randomUUID();
    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_email_incorrect = "john@example.pl";
    static final String test_pwHash = "c421";
    static final String test_pwHash_incorrect = "c4212";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;

    static final PlayerLoginDto test_playerLoginDto_incorrect = new PlayerLoginDto(
            test_email_incorrect,
            test_pwHash_incorrect
    );


    static final PlayerLoginDto test_playerLoginDto_correct = new PlayerLoginDto(
            test_email,
            test_pwHash
    );
    @Mock
    private SelectPlayerByEmailQueryInterface selectPlayerByEmailQueryInterface;

    @Mock
    private SaveLastLoginDateCommandInterface saveLastLoginDateCommandInterface;

    @InjectMocks
    private LoginPlayerUseCaseService loginPlayerUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        ReflectionTestUtils.setField(loginPlayerUseCaseService, "secret", "12341234123412341234123412341234");
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_nonexistent_player_and_throw_NoSuchElementException() throws HPLRValidationException {
        when(selectPlayerByEmailQueryInterface.selectPlayerByEmail(test_email_incorrect))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> loginPlayerUseCaseService.loginPlayer(test_playerLoginDto_incorrect));

    }

    @Test
    void get_existent_valid_player_with_wrong_password_and_throw_HPLRValidationException() throws HPLRValidationException{
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        PlayerEntity player = new PlayerEntity(
                test_playerId,
                test_name,
                test_email,
                bCryptPasswordEncoder.encode(test_pwHash),
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        when(selectPlayerByEmailQueryInterface.selectPlayerByEmail(test_email_incorrect))
                .thenReturn(Optional.of(PlayerMapper.toDto(player)));
        Assertions.assertNotNull(loginPlayerUseCaseService.getSecret());
        Assertions.assertFalse(loginPlayerUseCaseService.getSecret().isEmpty());
        Assertions.assertThrows(HPLRValidationException.class,
                () -> loginPlayerUseCaseService.loginPlayer(test_playerLoginDto_incorrect));

    }

    @Test
    void get_existent_valid_player_with_correct_password_and_succeed() throws HPLRValidationException{
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        PlayerEntity player = new PlayerEntity(
                test_playerId,
                test_name,
                test_email,
                bCryptPasswordEncoder.encode(test_pwHash),
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        when(selectPlayerByEmailQueryInterface.selectPlayerByEmail(test_email))
                .thenReturn(Optional.of(PlayerMapper.toDto(player)));
        try (MockedStatic<StringValidator> mockedStatic = mockStatic(StringValidator.class)) {
            Assertions.assertNotNull(loginPlayerUseCaseService.getSecret());
            Assertions.assertFalse(loginPlayerUseCaseService.getSecret().isEmpty());
            Assertions.assertDoesNotThrow(
                    () -> {
                        GetTokenResponseDto token = loginPlayerUseCaseService.loginPlayer(test_playerLoginDto_correct);
                        Assertions.assertNotNull(token);
                        Assertions.assertFalse(token.token().isEmpty());
                        verify(saveLastLoginDateCommandInterface, times(1)).saveLastLoginDate(any(), eq(test_playerId));

                    });
            mockedStatic.verify(()->StringValidator.validateString(loginPlayerUseCaseService.getSecret()), times(1));
        }


    }


}
