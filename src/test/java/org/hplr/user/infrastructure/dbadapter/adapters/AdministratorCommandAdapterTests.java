package org.hplr.user.infrastructure.dbadapter.adapters;

import org.hplr.user.core.model.AdministratorSnapshot;
import org.hplr.user.core.model.vo.AdministratorSecurity;
import org.hplr.user.core.model.vo.UserData;
import org.hplr.user.core.model.vo.UserId;
import org.hplr.user.infrastructure.dbadapter.repositories.AdministratorCommandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AdministratorCommandAdapterTests {

    private AutoCloseable closeable;

    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";

    @Mock
    private AdministratorCommandRepository administratorCommandRepository;

    @InjectMocks
    private AdministratorCommandAdapter administratorCommandAdapter;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_administrator_and_succeed() {
        AdministratorSnapshot test_administratorSnapshot = new AdministratorSnapshot(
                new UserId(UUID.randomUUID()),
                new UserData(
                        test_name,
                        test_nickname,
                        test_email,
                        test_motto),
                new AdministratorSecurity(
                        test_pwHash,
                        test_registrationTime,
                        test_lastLogin,
                        new ArrayList<>()
                ));
        Assertions.assertDoesNotThrow(
                () -> administratorCommandAdapter.saveAdministrator(test_administratorSnapshot));
        verify(administratorCommandRepository, atLeastOnce()).save(any());

    }

    @Test
    void save_last_login_date_for_administrator_and_succeed() {
        UUID test_administrator_uuid = UUID.randomUUID();
        LocalDateTime test_login_date = LocalDateTime.now();
        Assertions.assertDoesNotThrow(
                () -> administratorCommandAdapter.saveLastLoginDate(test_login_date, test_administrator_uuid));
        verify(administratorCommandRepository, atLeastOnce()).updateLastLoginDate(test_login_date, test_administrator_uuid);
    }


}
