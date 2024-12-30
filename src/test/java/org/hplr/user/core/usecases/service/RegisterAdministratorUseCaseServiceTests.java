package org.hplr.user.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.user.core.model.AdministratorSnapshot;
import org.hplr.user.core.usecases.port.dto.InitialAdministratorSaveDataDto;
import org.hplr.user.core.usecases.port.out.command.SaveAdministratorDataCommandInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class RegisterAdministratorUseCaseServiceTests {
    private AutoCloseable closeable;

    static final String test_name = "John";
    static final String test_email = "john@example.com";
    static final String test_motto = "c421";
    static final String test_pw1 = "c421";

    @Captor
    ArgumentCaptor<AdministratorSnapshot> administratorSnapshotCaptor;

    @Mock
    private SaveAdministratorDataCommandInterface mock_saveAdministratorDataCommandInterface;

    @InjectMocks
    private RegisterAdministratorUseCaseService registerAdministratorUseCase;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }


    private final InitialAdministratorSaveDataDto test_INITIALADMINISTRATOR_CORRECT= new InitialAdministratorSaveDataDto(
            test_name,
            test_name,
            test_email,
            test_motto,
            test_pw1,
            new ArrayList<>()
    );

    @Test
    void register_administrator_with_correct_password_and_succeed(){
        Assertions.assertDoesNotThrow(
                () -> {
                    UUID userId = registerAdministratorUseCase.registerAdministrator(test_INITIALADMINISTRATOR_CORRECT);
                    Assertions.assertNotNull(userId);
                }
        );
        verify(mock_saveAdministratorDataCommandInterface).saveAdministrator(administratorSnapshotCaptor.capture());
        verify(mock_saveAdministratorDataCommandInterface, times(1)).saveAdministrator(any());
        Assertions.assertFalse(administratorSnapshotCaptor.getValue().administratorSecurity().pwHash().isEmpty());
    }

}
