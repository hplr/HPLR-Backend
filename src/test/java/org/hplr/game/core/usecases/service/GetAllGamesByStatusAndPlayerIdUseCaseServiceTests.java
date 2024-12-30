package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.out.query.SelectGamesByStatusAndPlayerIdQueryInterface;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
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
class GetAllGamesByStatusAndPlayerIdUseCaseServiceTests {
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

    static final UUID test_playerId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Mock
    private SelectGamesByStatusAndPlayerIdQueryInterface selectGamesByStatusAndPlayerIdQueryInterface;

    @InjectMocks
    private GetAllGamesByStatusAndPlayerIdUseCaseService getAllGamesByStatusAndPlayerIdUseCaseService;

    @Test
    void select_games_by_status_find_none_and_succeed() {
        when(selectGamesByStatusAndPlayerIdQueryInterface
                .selectGamesByStatusAndPlayerId(Status.ARCHIVED,test_playerId))
                .thenReturn(List.of());
        List<GameSnapshot> result = getAllGamesByStatusAndPlayerIdUseCaseService.getAllGamesByStatusAndPlayerId(
                Status.ARCHIVED,
                test_playerId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void select_games_by_status_find_existing_and_succeed() {
        when(selectGamesByStatusAndPlayerIdQueryInterface
                .selectGamesByStatusAndPlayerId(Status.CREATED,
                        test_playerId))
                .thenReturn(List.of(GameDatabaseMapper.toDto(new GameEntity(
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
                                                0L,
                                                false
                                        )
                                )
                        ),
                        null
                ))));
        List<GameSnapshot> result = getAllGamesByStatusAndPlayerIdUseCaseService.getAllGamesByStatusAndPlayerId(
                Status.ARCHIVED,
                test_playerId
        );
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        result = getAllGamesByStatusAndPlayerIdUseCaseService.getAllGamesByStatusAndPlayerId(
                Status.CREATED,
                test_playerId
        );
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
    }
}
