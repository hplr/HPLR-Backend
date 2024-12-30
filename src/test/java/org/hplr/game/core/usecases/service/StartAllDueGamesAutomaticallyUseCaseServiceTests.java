package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.GameValidator;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.out.command.StartAllDueGamesCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGamesQueryInterface;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class StartAllDueGamesAutomaticallyUseCaseServiceTests {
    private AutoCloseable closeable;
    static final UUID test_gameId = UUID.randomUUID();
    static final UUID test_gameSideId = UUID.randomUUID();
    static final UUID test_gameSideId_second = UUID.randomUUID();
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
    private GameSelectDto mock_GameSelectDto;

    @Mock
    private SelectAllGamesQueryInterface selectAllGamesQueryInterface;

    @Mock
    private StartAllDueGamesCommandInterface startAllDueGamesCommandInterface;

    @InjectMocks
    private StartAllDueGamesAutomaticallyUseCaseService startAllDueGamesAutomaticallyUseCaseService;

    @Captor
    private ArgumentCaptor<List<GameSnapshot>> gameSnapshotListCaptor;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mock_GameSelectDto = GameDatabaseMapper.toDto(new GameEntity(
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
                Status.AWAITING,
                new GameSideEntity(
                        null,
                        test_gameSideId,
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
                new GameSideEntity(
                        null,
                        test_gameSideId_second,
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
                )
        ));
        when(selectAllGamesQueryInterface.selectAllGames())
                .thenReturn(List.of(mock_GameSelectDto));
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_all_games_fail_validation_and_start_none(){
        try (MockedStatic<GameValidator> mockedStatic = mockStatic(GameValidator.class)) {
            mockedStatic.when(()->GameValidator.validateStartingGame(any())).thenThrow(HPLRValidationException.class);
            startAllDueGamesAutomaticallyUseCaseService.startGameAutomatically();
            verify(startAllDueGamesCommandInterface,times(1)).startAllDueGames(List.of());
        }
    }

    @Test
    void get_all_games_pass_validation_wrong_status_and_start_one(){
        when(selectAllGamesQueryInterface.selectAllGames())
                .thenReturn(List.of(mock_GameSelectDto, GameDatabaseMapper.toDto(new GameEntity(
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
                        Status.CANCELLED,
                        new GameSideEntity(
                                null,
                                test_gameSideId,
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
                        new GameSideEntity(
                                null,
                                test_gameSideId_second,
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
                        )
                ))));
        startAllDueGamesAutomaticallyUseCaseService.startGameAutomatically();
        verify(startAllDueGamesCommandInterface).startAllDueGames(gameSnapshotListCaptor.capture());
        verify(startAllDueGamesCommandInterface,times(1)).startAllDueGames(anyList());
        List<GameSnapshot> gameSnapshotList = gameSnapshotListCaptor.getValue();
        Assertions.assertNotNull(gameSnapshotList);
        Assertions.assertFalse(gameSnapshotList.isEmpty());
        Assertions.assertEquals(1,gameSnapshotList.size());
    }

    @Test
    void get_all_games_pass_validation_wrong_date_and_start_one(){
        when(selectAllGamesQueryInterface.selectAllGames())
                .thenReturn(List.of(mock_GameSelectDto, GameDatabaseMapper.toDto(new GameEntity(
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
                        LocalDateTime.of(2025,12,1,12,12,12),
                        test_gameEnd,
                        false,
                        Status.AWAITING,
                        new GameSideEntity(
                                null,
                                test_gameSideId,
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
                        new GameSideEntity(
                                null,
                                test_gameSideId_second,
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
                        )
                ))));
        startAllDueGamesAutomaticallyUseCaseService.startGameAutomatically();
        verify(startAllDueGamesCommandInterface).startAllDueGames(gameSnapshotListCaptor.capture());
        verify(startAllDueGamesCommandInterface,times(1)).startAllDueGames(anyList());
        List<GameSnapshot> gameSnapshotList = gameSnapshotListCaptor.getValue();
        Assertions.assertNotNull(gameSnapshotList);
        Assertions.assertFalse(gameSnapshotList.isEmpty());
        Assertions.assertEquals(1,gameSnapshotList.size());
    }

    @Test
    void get_all_games_pass_validation_and_start_one(){
            startAllDueGamesAutomaticallyUseCaseService.startGameAutomatically();
            verify(startAllDueGamesCommandInterface).startAllDueGames(gameSnapshotListCaptor.capture());
            verify(startAllDueGamesCommandInterface,times(1)).startAllDueGames(anyList());
            List<GameSnapshot> gameSnapshotList = gameSnapshotListCaptor.getValue();
            Assertions.assertNotNull(gameSnapshotList);
            Assertions.assertFalse(gameSnapshotList.isEmpty());
            Assertions.assertEquals(1,gameSnapshotList.size());
    }

}
