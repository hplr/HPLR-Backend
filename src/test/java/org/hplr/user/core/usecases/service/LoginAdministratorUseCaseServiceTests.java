package org.hplr.user.core.usecases.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.hplr.library.core.model.StringValidator;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.user.core.model.vo.AdministratorRole;
import org.hplr.user.core.usecases.port.dto.GetTokenResponseDto;
import org.hplr.user.core.usecases.port.dto.AdministratorLoginDto;
import org.hplr.user.core.usecases.port.out.command.SaveLastAdministratorLoginDateCommandInterface;
import org.hplr.user.core.usecases.port.out.query.SelectAdministratorByEmailQueryInterface;
import org.hplr.user.infrastructure.dbadapter.entities.AdministratorEntity;
import org.hplr.user.infrastructure.dbadapter.entities.RoleEntity;
import org.hplr.user.infrastructure.dbadapter.mappers.AdministratorMapper;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@Slf4j
class LoginAdministratorUseCaseServiceTests {

    private AutoCloseable closeable;

    static final UUID test_administratorId = UUID.randomUUID();
    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_email_incorrect = "john@example.pl";
    static final String test_pwHash = "c421";
    static final String test_pwHash_incorrect = "c4212";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final List<RoleEntity> test_roles = List.of(new RoleEntity(AdministratorRole.TOURNAMENT_START));

    static final AdministratorLoginDto test_administratorLoginDto_incorrect = new AdministratorLoginDto(
            test_email_incorrect,
            test_pwHash_incorrect
    );


    static final AdministratorLoginDto test_administratorLoginDto_correct = new AdministratorLoginDto(
            test_email,
            test_pwHash
    );
    @Mock
    private SelectAdministratorByEmailQueryInterface selectadministratorByEmailQueryInterface;

    @Mock
    private SaveLastAdministratorLoginDateCommandInterface saveLastAdministratorLoginDateCommandInterface;

    @InjectMocks
    private LoginAdministratorUseCaseService loginadministratorUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        ReflectionTestUtils.setField(loginadministratorUseCaseService, "secret", "12341234123412341234123412341234");
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_nonexistent_administrator_and_throw_NoSuchElementException() throws HPLRValidationException {
        when(selectadministratorByEmailQueryInterface.selectAdministratorByEmail(test_email_incorrect))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> loginadministratorUseCaseService.loginAdministrator(test_administratorLoginDto_incorrect));

    }

    @Test
    void get_existent_valid_administrator_with_wrong_password_and_throw_HPLRValidationException() throws HPLRValidationException{
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        AdministratorEntity administrator = new AdministratorEntity(
                test_administratorId,
                test_name,
                test_email,
                bCryptPasswordEncoder.encode(test_pwHash),
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_roles);
        when(selectadministratorByEmailQueryInterface.selectAdministratorByEmail(test_email_incorrect))
                .thenReturn(Optional.of(AdministratorMapper.toDto(administrator)));
        Assertions.assertNotNull(loginadministratorUseCaseService.getSecret());
        Assertions.assertFalse(loginadministratorUseCaseService.getSecret().isEmpty());
        Assertions.assertThrows(HPLRValidationException.class,
                () -> loginadministratorUseCaseService.loginAdministrator(test_administratorLoginDto_incorrect));

    }

    @Test
    void get_existent_valid_administrator_with_correct_password_and_succeed() throws HPLRValidationException{
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        AdministratorEntity administrator = new AdministratorEntity(
                test_administratorId,
                test_name,
                test_email,
                bCryptPasswordEncoder.encode(test_pwHash),
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_roles);
        when(selectadministratorByEmailQueryInterface.selectAdministratorByEmail(test_email))
                .thenReturn(Optional.of(AdministratorMapper.toDto(administrator)));
        try (MockedStatic<StringValidator> mockedStatic = mockStatic(StringValidator.class)) {
            Assertions.assertNotNull(loginadministratorUseCaseService.getSecret());
            Assertions.assertFalse(loginadministratorUseCaseService.getSecret().isEmpty());
            Assertions.assertDoesNotThrow(
                    () -> {
                        GetTokenResponseDto token = loginadministratorUseCaseService.loginAdministrator(test_administratorLoginDto_correct);
                        Assertions.assertNotNull(token);
                        Assertions.assertFalse(token.token().isEmpty());
                        verify(saveLastAdministratorLoginDateCommandInterface, times(1)).saveLastLoginDate(any(), eq(test_administratorId));
                        Claims claims = Jwts.parserBuilder().setSigningKey(loginadministratorUseCaseService.getSecret().getBytes()).build().parseClaimsJws(token.token()).getBody();
                        Assertions.assertEquals("TOURNAMENT_START,",claims.get("permissions").toString());
                    });
            mockedStatic.verify(()->StringValidator.validateString(loginadministratorUseCaseService.getSecret()), times(1));
        }


    }


}
