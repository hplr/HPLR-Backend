package org.hplr.core.usecases.service;

import org.hplr.core.enums.Allegiance;
import org.hplr.core.enums.Status;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.hplr.exception.HPLRValidationException;
import org.hplr.infrastructure.dbadapter.entities.*;
import org.hplr.infrastructure.dbadapter.mappers.GameDatabaseMapper;
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

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetGameByIDUseCaseServiceTests {

    private AutoCloseable closeable;

    static final UUID test_gameId = UUID.randomUUID();
    static final String test_name = "location";
    static final String test_country = "Poland";
    static final String test_city = "Łódź";
    static final String test_street = "Duża";
    static final String test_houseNumber = "23";
    static final Double test_latitude = 23.0;
    static final Double test_longitude = 23.0;
    static final String test_mission = "mission";
    static final String test_deployment = "deployment";
    static final Long test_size = 1250L;
    static final Integer test_turnLength = 6;
    static final Integer test_timeLength = 1;
    static final LocalDateTime test_gameStart = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_gameEnd = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_player_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;

    @Mock
    private SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;

    @InjectMocks
    private GetGameByIDUseCaseService getGameByIDUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_nonexistent_game_and_throw_NoSuchElementException() throws HPLRValidationException {
        UUID failedGameId = UUID.randomUUID();
        when(selectGameByGameIdQueryInterface.selectGameByGameId(failedGameId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> getGameByIDUseCaseService.getGameByID(failedGameId));

    }

    @Test
    void get_existent_valid_game_and_succeed() throws HPLRValidationException {
        GameEntity gameEntity = new GameEntity(
                null,
                null,
                null,
                test_gameId,
                new LocationEntity(
                        null,
                        UUID.randomUUID(),
                        test_name,
                        false,
                        new LocationGeoDataEntity(
                                null,
                                test_country,
                                test_city,
                                test_street,
                                test_houseNumber,
                                test_longitude,
                                test_latitude
                        )
                ),
                new GameMissionEntity(
                        null,
                        test_mission
                ),
                new GameDeploymentEntity(
                        null,
                        test_deployment
                ),
                test_size,
                test_turnLength,
                test_timeLength,
                test_gameStart,
                test_gameEnd,
                false,
                Status.CREATED,
                new GameSideEntity(
                        null,
                        UUID.randomUUID(),
                        Allegiance.LOYALIST,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_gameId,
                                                test_player_name,
                                                test_email,
                                                test_pwHash,
                                                test_registrationTime,
                                                test_lastLogin,
                                                test_nickname,
                                                test_motto,
                                                test_score
                                        ),
                                        test_score,
                                        new GameArmyEntity(
                                                null,
                                                new GameArmyTypeEntity(
                                                        null,
                                                        "IH"
                                                ),
                                                test_name,
                                                test_score
                                        ),
                                        null
                                )
                        ),
                        null,
                        List.of(
                                new GameTurnScoreEntity(
                                        null,
                                        1L,
                                        0L
                                )
                        )
                ),
                null
        );
        when(selectGameByGameIdQueryInterface.selectGameByGameId(test_gameId))
                .thenReturn(Optional.of(GameDatabaseMapper.toDto(gameEntity)));
        GameSnapshot gameSnapshot = Assertions.assertDoesNotThrow(
                () -> getGameByIDUseCaseService.getGameByID(test_gameId)
        );
        Assertions.assertNotNull(gameSnapshot);
    }
}
