package org.hplr.game.infrastructure.dbadapter.adapters;


import lombok.extern.slf4j.Slf4j;
import org.hplr.elo.core.model.vo.Elo;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSide;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.vo.*;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.repositories.*;

import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.library.exception.LocationCalculationException;

import org.hplr.location.core.model.Location;
import org.hplr.location.core.usecases.port.dto.LocationSaveDto;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.location.infrastructure.dbadapter.repositories.LocationRepository;

import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@Slf4j
class GameCommandAdapterTests {

    private AutoCloseable closeable;

    static final UUID test_gameId = UUID.randomUUID();
    static final String test_name = "location";
    static final String test_country = "Poland";
    static final String test_city = "Łódź";
    static final String test_street = "Duża";
    static final String test_houseNumber = "23";
    static final String test_mission = "mission";
    static final String test_deployment = "deployment";
    static final Long test_size = 1250L;
    static final Integer test_turnLength = 6;
    static final Duration test_timeLength = Duration.ofHours(1L);
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
    static final Double test_latitude = 23.0;
    static final Double test_longitude = 23.0;

    @Mock
    private LocationRepository mock_locationRepository;
    @Mock
    private PlayerQueryRepository mock_playerQueryRepository;
    @Mock
    private GameRepository mock_gameRepository;
    @Mock
    private GameMissionRepository mock_gameMissionRepository;
    @Mock
    private GameDeploymentRepository mock_gameDeploymentRepository;
    @Mock
    private GameArmyTypeRepository mock_gameArmyTypeRepository;
    @Mock
    private GameHistoricalEloRepository mock_gameHistoricalEloRepository;
    @Mock
    private GameSnapshot mock_gameSnapshot;

    @InjectMocks
    @Spy
    private GameCommandAdapter gameCommandAdapter;

    @Captor
    private ArgumentCaptor<GameEntity> captor_GameEntity;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_game_fetch_not_enough_players_and_throw_IllegalStateException() {
        when(mock_playerQueryRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> gameCommandAdapter.saveGame(mock_gameSnapshot)
        );
    }

    @Test
    void save_game_fetch_no_army_types_and_throw_IllegalStateException() {
        when(mock_playerQueryRepository.findAll()).thenReturn(List.of(
                new PlayerEntity(), new PlayerEntity()
        ));
        when(mock_gameArmyTypeRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> gameCommandAdapter.saveGame(mock_gameSnapshot)
        );
    }

    @Test
    void save_game_and_succeed() throws LocationCalculationException, HPLRValidationException {
        UUID test_player1Id = UUID.randomUUID();
        UUID test_player2Id = UUID.randomUUID();
        PlayerEntity test_playerEntity1 = new PlayerEntity(
                test_player1Id,
                test_player_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score
        );
        PlayerEntity test_playerEntity2 = new PlayerEntity(
                test_player2Id,
                test_player_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score
        );
        GameArmyTypeEntity test_gameArmyTypeEntity = new GameArmyTypeEntity(
                1L,
                "IH"
        );
        GameArmyTypeEntity test_gameArmyType2Entity = new GameArmyTypeEntity(
                2L,
                "IH"
        );
        mock_gameSnapshot = new GameSnapshot(
                new GameId(test_gameId),
                new GameLocation(Location.fromDto(new LocationSaveDto(
                        test_name,
                        test_country,
                        test_city,
                        test_street,
                        test_houseNumber,
                        false
                ))),
                new GameData(
                        new GameMission(test_mission),
                        new GameDeployment(test_deployment),
                        test_size,
                        test_turnLength,
                        test_timeLength,
                        test_gameStart,
                        test_gameEnd,
                        false
                ),
                0L,
                Status.AWAITING,
                new GameSideSnapshot(GameSide.fromDto(
                        Allegiance.LOYALIST,
                        List.of(
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_player1Id,
                                                        test_player_name,
                                                        test_nickname,
                                                        test_email,
                                                        test_motto,
                                                        test_score,
                                                        test_pwHash,
                                                        test_registrationTime,
                                                        test_lastLogin
                                                )
                                        ),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null
                                )
                        ),
                        test_turnLength)
                ),
                new GameSideSnapshot(GameSide.fromDto(
                        Allegiance.LOYALIST,
                        List.of(
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_player2Id,
                                                        test_player_name,
                                                        test_nickname,
                                                        test_email,
                                                        test_motto,
                                                        test_score,
                                                        test_pwHash,
                                                        test_registrationTime,
                                                        test_lastLogin
                                                )
                                        ),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null
                                )
                        ),
                        test_turnLength)
                )
        );

        when(mock_playerQueryRepository.findAll()).thenReturn(List.of(
                test_playerEntity1, test_playerEntity2
        ));
        when(mock_gameArmyTypeRepository.findAll()).thenReturn(
                List.of(test_gameArmyTypeEntity, test_gameArmyType2Entity)
        );
        Assertions.assertNotNull(mock_gameSnapshot.secondGameSide());
        Assertions.assertDoesNotThrow(
                () -> gameCommandAdapter.saveGame(mock_gameSnapshot)
        );
        verify(mock_locationRepository, times(1)).findByLocationId(any());
        verify(mock_gameMissionRepository, times(1)).findByName(any());
        verify(mock_gameDeploymentRepository, times(1)).findByName(any());
        verify(mock_gameRepository).save(captor_GameEntity.capture());
        Assertions.assertNotNull(captor_GameEntity.getValue().getLocationEntity());
        Assertions.assertNotNull(captor_GameEntity.getValue().getGameDeploymentEntity());
        Assertions.assertNotNull(captor_GameEntity.getValue().getGameMissionEntity());
        Assertions.assertNotNull(captor_GameEntity.getValue().getFirstGameSide());
        Assertions.assertNotNull(captor_GameEntity.getValue().getSecondGameSide());
    }

    @Test
    void start_due_games_and_succeed() {
        UUID test_player1Id = UUID.randomUUID();
        UUID test_player2Id = UUID.randomUUID();
        mock_gameSnapshot = new GameSnapshot(
                new GameId(test_gameId),
                new GameLocation(Location.fromDto(new LocationSaveDto(
                        test_name,
                        test_country,
                        test_city,
                        test_street,
                        test_houseNumber,
                        false
                ))),
                new GameData(
                        new GameMission(test_mission),
                        new GameDeployment(test_deployment),
                        test_size,
                        test_turnLength,
                        test_timeLength,
                        test_gameStart,
                        test_gameEnd,
                        false
                ),
                0L,
                Status.AWAITING,
                new GameSideSnapshot(GameSide.fromDto(
                        Allegiance.LOYALIST,
                        List.of(
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_player1Id,
                                                        test_player_name,
                                                        test_nickname,
                                                        test_email,
                                                        test_motto,
                                                        test_score,
                                                        test_pwHash,
                                                        test_registrationTime,
                                                        test_lastLogin
                                                )
                                        ),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null
                                )
                        ),
                        test_turnLength)
                ),
                new GameSideSnapshot(GameSide.fromDto(
                        Allegiance.LOYALIST,
                        List.of(
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_player2Id,
                                                        test_player_name,
                                                        test_nickname,
                                                        test_email,
                                                        test_motto,
                                                        test_score,
                                                        test_pwHash,
                                                        test_registrationTime,
                                                        test_lastLogin
                                                )
                                        ),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null
                                )
                        ),
                        test_turnLength)
                )
        );
        gameCommandAdapter.startAllDueGames(List.of(mock_gameSnapshot));
        verify(mock_gameRepository, times(1))
                .startAllDueGames(List.of(mock_gameSnapshot.gameId().gameId()));

    }

    @Test
    void save_finished_game_that_cannot_be_fetched_and_throw_HPLRIllegalStateException() {
        UUID test_gameId_temp = UUID.randomUUID();
        when(mock_gameSnapshot.gameId()).thenReturn(new GameId(test_gameId_temp));
        when(mock_gameRepository.findByGameId(mock_gameSnapshot.gameId().gameId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> gameCommandAdapter.saveFinishedGame(mock_gameSnapshot, null));
    }

    @Test
    void save_finished_game_and_succeed() {
        UUID test_player1Id = UUID.randomUUID();
        UUID test_player2Id = UUID.randomUUID();
        mock_gameSnapshot = new GameSnapshot(
                new GameId(test_gameId),
                new GameLocation(Location.fromDto(new LocationSaveDto(
                        test_name,
                        test_country,
                        test_city,
                        test_street,
                        test_houseNumber,
                        false
                ))),
                new GameData(
                        new GameMission(test_mission),
                        new GameDeployment(test_deployment),
                        test_size,
                        test_turnLength,
                        test_timeLength,
                        test_gameStart,
                        test_gameEnd,
                        false
                ),
                0L,
                Status.FINISHED,
                new GameSideSnapshot(GameSide.fromDto(
                        Allegiance.LOYALIST,
                        List.of(
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_player1Id,
                                                        test_player_name,
                                                        test_nickname,
                                                        test_email,
                                                        test_motto,
                                                        test_score,
                                                        test_pwHash,
                                                        test_registrationTime,
                                                        test_lastLogin
                                                )
                                        ),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null
                                )
                        ),
                        test_turnLength)
                ),
                new GameSideSnapshot(GameSide.fromDto(
                        Allegiance.LOYALIST,
                        List.of(
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_player2Id,
                                                        test_player_name,
                                                        test_nickname,
                                                        test_email,
                                                        test_motto,
                                                        test_score,
                                                        test_pwHash,
                                                        test_registrationTime,
                                                        test_lastLogin
                                                )
                                        ),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null
                                )
                        ),
                        test_turnLength)
                )
        );
        GameHistoricalElo gameHistoricalElo = new GameHistoricalElo(
                new GameId(test_gameId),
                new Elo(mock_gameSnapshot
                        .firstGameSide()
                        .gameSidePlayerDataList()
                        .getFirst()
                        .player()
                        .playerRanking()
                        .score()
                ),
                new Elo(mock_gameSnapshot
                        .secondGameSide()
                        .gameSidePlayerDataList()
                        .getFirst()
                        .player()
                        .playerRanking()
                        .score()
                )
        );
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
                test_timeLength.toHoursPart(),
                test_gameStart,
                test_gameEnd,
                false,
                Status.AWAITING,
                new GameSideEntity(
                        null,
                        UUID.randomUUID(),
                        Allegiance.LOYALIST,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_player1Id,
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
                        UUID.randomUUID(),
                        Allegiance.LOYALIST,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_player2Id,
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
        );
        when(mock_gameRepository.findByGameId(mock_gameSnapshot.gameId().gameId()))
                .thenReturn(Optional.of(gameEntity));
        Assertions.assertDoesNotThrow(
                () -> {
                    List<Integer> results = gameCommandAdapter.saveFinishedGame(mock_gameSnapshot, gameHistoricalElo);
                    Assertions.assertNotNull(results);
                    Assertions.assertFalse(results.isEmpty());
                    results.forEach(result ->
                            Assertions.assertEquals(1, result));
                    verify(mock_gameRepository, times(1)).findByGameId(mock_gameSnapshot.gameId().gameId());
                    verify(mock_gameRepository, times(1)).save(any());
                    verify(gameCommandAdapter, times(1)).updateScore(any(), any(),eq(true));
                    verify(gameCommandAdapter, times(1)).updateScore(any(), any(),eq(false));
                    verify(mock_gameRepository).save(captor_GameEntity.capture());
                    verify(mock_gameHistoricalEloRepository, times(1)).save(any());
                    Assertions.assertEquals(Status.FINISHED, captor_GameEntity.getValue().getStatus());


                }
        );
    }

    @Test
    void start_game_and_succeed() {
        UUID test_gameId_temp = UUID.randomUUID();
        when(mock_gameSnapshot.gameId()).thenReturn(new GameId(test_gameId_temp));
        gameCommandAdapter.startGame(mock_gameSnapshot);
        verify(mock_gameRepository, times(1)).startGame(mock_gameSnapshot.gameId().gameId());
    }

}
