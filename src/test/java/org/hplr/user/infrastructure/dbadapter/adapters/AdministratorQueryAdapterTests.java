package org.hplr.user.infrastructure.dbadapter.adapters;

import org.hplr.user.core.usecases.port.dto.AdministratorSelectDto;
import org.hplr.user.infrastructure.dbadapter.entities.AdministratorEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.AdministratorQueryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AdministratorQueryAdapterTests {

    private AutoCloseable closeable;

    static final UUID test_administratorId = UUID.randomUUID();
    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";

    @Mock
    private AdministratorQueryRepository administratorQueryRepository;

    @InjectMocks
    private AdministratorQueryAdapter administratorQueryAdapter;


    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void get_nonexistent_administrator_by_email_and_throw_NoSuchElementException() {
        String failedUserEmail = "noEmail@email.com";
        when(administratorQueryRepository.findByEmail(failedUserEmail))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> administratorQueryAdapter.selectAdministratorByEmail(failedUserEmail));
        verify(administratorQueryRepository,atLeastOnce()).findByEmail(any());
    }

    @Test
    void get_existent_administrator_by_email_and_succeed() {
        AdministratorEntity administrator = new AdministratorEntity(
                test_administratorId,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                new ArrayList<>());

        when(administratorQueryRepository.findByEmail(test_email))
                .thenReturn(Optional.of(administrator));
        Assertions.assertDoesNotThrow(
                () -> {
                    Optional<AdministratorSelectDto> administratorSelectDto;
                    administratorSelectDto = administratorQueryAdapter.selectAdministratorByEmail(test_email);
                    Assertions.assertTrue(administratorSelectDto.isPresent());
                });
        verify(administratorQueryRepository,atLeastOnce()).findByEmail(test_email);

    }
}
