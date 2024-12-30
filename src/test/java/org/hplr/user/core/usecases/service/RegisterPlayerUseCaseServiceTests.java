package org.hplr.user.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.user.core.usecases.port.dto.InitialPlayerSaveDataDto;
import org.hplr.user.core.usecases.port.out.command.SavePlayerDataCommandInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class RegisterPlayerUseCaseServiceTests {
    private AutoCloseable closeable;

    static final String test_name = "John";
    static final String test_name_blank = "";
    static final String test_email = "john@example.com";
    static final String test_motto = "c421";
    static final String test_pw1 = "c421";
    static final String test_pw2 = "c422";

    @Mock
    private SavePlayerDataCommandInterface mock_savePlayerDataCommandInterface;

    @InjectMocks
    private RegisterPlayerUseCaseService registerPlayerUseCase;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    private final InitialPlayerSaveDataDto test_INITIALPLAYER_WRONG = new InitialPlayerSaveDataDto(
            test_name,
            test_name,
            test_email,
            test_motto,
            test_pw1,
            test_pw2
    );

    private final InitialPlayerSaveDataDto test_INITIALPLAYER_blank_name = new InitialPlayerSaveDataDto(
            test_name_blank,
            test_name_blank,
            test_email,
            test_motto,
            test_pw1,
            test_pw1
    );

    private final InitialPlayerSaveDataDto test_INITIALPLAYER_CORRECT= new InitialPlayerSaveDataDto(
            test_name,
            test_name,
            test_email,
            test_motto,
            test_pw1,
            test_pw1
    );

    @Test
    void register_player_with_different_passwords_and_fail(){
        HPLRIllegalStateException exception_password = Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> registerPlayerUseCase.registerPlayer(test_INITIALPLAYER_WRONG)
        );
        Assertions.assertEquals("Provided passwords do not match", exception_password.getMessage());
        verify(mock_savePlayerDataCommandInterface, times(0)).savePlayer(any());
    }

    @Test
    void register_player_with_no_name_and_throw_HPLRValidationError(){
        Assertions.assertThrows(HPLRValidationException.class,
                () -> registerPlayerUseCase.registerPlayer(test_INITIALPLAYER_blank_name)
        );
        verify(mock_savePlayerDataCommandInterface, times(0)).savePlayer(any());
    }

    @Test
    void register_player_with_correct_password_and_succeed(){
        Assertions.assertDoesNotThrow(
                () -> {
                    UUID userId = registerPlayerUseCase.registerPlayer(test_INITIALPLAYER_CORRECT);
                    Assertions.assertNotNull(userId);
                }
        );
        verify(mock_savePlayerDataCommandInterface, times(1)).savePlayer(any());

    }

}
