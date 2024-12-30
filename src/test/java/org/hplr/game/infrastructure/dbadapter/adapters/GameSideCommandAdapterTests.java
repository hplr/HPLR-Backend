package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.elo.core.model.vo.Score;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSide;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.vo.*;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.repositories.GameArmyTypeRepository;
import org.hplr.game.infrastructure.dbadapter.repositories.GameRepository;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.location.core.model.Location;
import org.hplr.location.core.usecases.port.dto.LocationSaveDto;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class GameSideCommandAdapterTests {
    private AutoCloseable closeable;
    static final UUID test_player1Id = UUID.randomUUID();
    static final UUID test_player2Id = UUID.randomUUID();
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
    static final Long test_turn_score = 10L;

    static final PlayerEntity test_playerEntity1 = new PlayerEntity(
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
    static final GameArmyTypeEntity test_gameArmyTypeEntity = new GameArmyTypeEntity(
            1L,
            "IH"
    );
    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerQueryRepository playerQueryRepository;

    @Mock
    private GameArmyTypeRepository gameArmyTypeRepository;

    @Mock
    private GameSnapshot mock_gameSnapshot;

    @InjectMocks
    private GameSideCommandAdapter gameSideCommandAdapter;

    @Captor
    private ArgumentCaptor<GameEntity> captor_GameEntity;

    GameEntity gameEntity;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void saveGameSecondSide_find_no_players_and_throw_HPLRIllegalStateException() {
        when(playerQueryRepository.findAll()).thenReturn(List.of());
        HPLRIllegalStateException ex = Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> gameSideCommandAdapter.saveGameSecondSide(mock_gameSnapshot));
        Assertions.assertEquals("Not enough players!", ex.getMessage());
    }

    @Test
    void saveGameSecondSide_find_no_armies_and_throw_HPLRIllegalStateException() {
        when(playerQueryRepository.findAll()).thenReturn(List.of(test_playerEntity1));
        when(gameArmyTypeRepository.findAll()).thenReturn(List.of());
        HPLRIllegalStateException ex = Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> gameSideCommandAdapter.saveGameSecondSide(mock_gameSnapshot));
        Assertions.assertEquals("No army types!", ex.getMessage());
    }

    @Test
    void saveGameSecondSide_find_no_game_and_throw_NoSuchElementException() {
        when(playerQueryRepository.findAll()).thenReturn(List.of(test_playerEntity1));
        when(gameArmyTypeRepository.findAll()).thenReturn(List.of(test_gameArmyTypeEntity));
        when(mock_gameSnapshot.gameId()).thenReturn(new GameId(test_gameId));
        when(gameRepository.findByGameId(test_gameId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> gameSideCommandAdapter.saveGameSecondSide(mock_gameSnapshot)
        );

    }

    @Test
    void saveGameSecondSide_and_succeed() {
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
                Status.CREATED,
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
        gameEntity = new GameEntity(
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
                Status.CREATED,
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
                null
        );
        when(playerQueryRepository.findAll()).thenReturn(List.of(test_playerEntity1));
        when(gameArmyTypeRepository.findAll()).thenReturn(List.of(test_gameArmyTypeEntity));
        when(gameRepository.findByGameId(test_gameId)).thenReturn(Optional.of(gameEntity));

        Assertions.assertDoesNotThrow(
                () -> gameSideCommandAdapter.saveGameSecondSide(mock_gameSnapshot));
        verify(gameRepository, times(1)).findByGameId(test_gameId);
        verify(gameRepository).save(captor_GameEntity.capture());
        verify(gameRepository, times(1)).save(any());
        GameEntity gameEntity_saved = captor_GameEntity.getValue();
        Assertions.assertEquals(Status.AWAITING, gameEntity_saved.getStatus());
        Assertions.assertNotNull(gameEntity_saved.getSecondGameSide());
        Assertions.assertFalse(gameEntity_saved.getSecondGameSide().getGamePlayerDataEntityList().isEmpty());
    }

    @Test
    void save_score_find_no_game_and_throw_NoSuchElementException() {
        Score score = new Score(1L, test_turn_score, false);
        when(playerQueryRepository.findAll()).thenReturn(List.of(test_playerEntity1));
        when(gameArmyTypeRepository.findAll()).thenReturn(List.of(test_gameArmyTypeEntity));
        when(mock_gameSnapshot.gameId()).thenReturn(new GameId(test_gameId));
        when(gameRepository.findByGameId(test_gameId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> gameSideCommandAdapter.saveScore(mock_gameSnapshot, score, true)
        );
    }


    @Test
    void save_score_find_game_get_wrong_turn_and_throw_NoSuchElementException() {
        Score score = new Score(7L, test_turn_score, false);
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
        gameEntity = new GameEntity(
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
        when(playerQueryRepository.findAll()).thenReturn(List.of(test_playerEntity1));
        when(gameArmyTypeRepository.findAll()).thenReturn(List.of(test_gameArmyTypeEntity));
        when(gameRepository.findByGameId(test_gameId)).thenReturn(Optional.of(gameEntity));
        Assertions.assertThrows(NoSuchElementException.class,
                () ->
                        gameSideCommandAdapter.saveScore(mock_gameSnapshot, score, true)
        );
        verify(gameRepository, times(1)).findByGameId(test_gameId);
        verify(gameRepository, times(0)).save(any());
        Assertions.assertThrows(NoSuchElementException.class,
                () ->
                        gameSideCommandAdapter.saveScore(mock_gameSnapshot, score, false)
        );
        verify(gameRepository, times(2)).findByGameId(test_gameId);
        verify(gameRepository, times(0)).save(any());

    }

    @Test
    void save_score_find_game_get_first_side_and_succeed() {
        Score score = new Score(1L, test_turn_score, true);
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
        gameEntity = new GameEntity(
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
        when(playerQueryRepository.findAll()).thenReturn(List.of(test_playerEntity1));
        when(gameArmyTypeRepository.findAll()).thenReturn(List.of(test_gameArmyTypeEntity));
        when(gameRepository.findByGameId(test_gameId)).thenReturn(Optional.of(gameEntity));
        Assertions.assertDoesNotThrow(
                () ->
                {
                    gameSideCommandAdapter.saveScore(mock_gameSnapshot, score, true);
                    verify(gameRepository,times(1)).findByGameId(test_gameId);
                    verify(gameRepository, times(1)).save(any());
                    verify(gameRepository).save(captor_GameEntity.capture());
                    GameEntity gameEntity_captured = captor_GameEntity.getValue();
                    Assertions.assertNotNull(
                            gameEntity_captured
                                    .getFirstGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTurnScore()
                    );
                    Assertions.assertEquals(
                            score.turnScore(),
                            gameEntity_captured
                                    .getFirstGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTurnScore()
                    );
                    Assertions.assertNotNull(
                            gameEntity_captured
                                    .getFirstGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTabled()
                    );
                    Assertions.assertEquals(
                            score.tabled(),
                            gameEntity_captured
                                    .getFirstGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTabled()
                    );
                    gameEntity_captured.getSecondGameSide().getTurnScoreEntityList().forEach(
                            turn -> {
                                Assertions.assertEquals(0,turn.getTurnScore());
                                Assertions.assertFalse(turn.getTabled());
                            }
                    );
                }
        );

    }

    @Test
    void save_score_find_game_get_second_side_and_succeed() {
        Score score = new Score(1L, test_turn_score, true);
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
        gameEntity = new GameEntity(
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
        when(playerQueryRepository.findAll()).thenReturn(List.of(test_playerEntity1));
        when(gameArmyTypeRepository.findAll()).thenReturn(List.of(test_gameArmyTypeEntity));
        when(gameRepository.findByGameId(test_gameId)).thenReturn(Optional.of(gameEntity));
        Assertions.assertDoesNotThrow(
                () ->
                {
                    gameSideCommandAdapter.saveScore(mock_gameSnapshot, score, false);
                    verify(gameRepository,times(1)).findByGameId(test_gameId);
                    verify(gameRepository, times(1)).save(any());
                    verify(gameRepository).save(captor_GameEntity.capture());
                    GameEntity gameEntity_captured = captor_GameEntity.getValue();
                    Assertions.assertNotNull(
                            gameEntity_captured
                                    .getSecondGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTurnScore()
                    );
                    Assertions.assertEquals(
                            score.turnScore(),
                            gameEntity_captured
                                    .getSecondGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTurnScore()
                    );
                    Assertions.assertNotNull(
                            gameEntity_captured
                                    .getSecondGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTabled()
                    );
                    Assertions.assertEquals(
                            score.tabled(),
                            gameEntity_captured
                                    .getSecondGameSide()
                                    .getTurnScoreEntityList()
                                    .get(Math.toIntExact(score.turnNumber()-1))
                                    .getTabled()
                    );
                    gameEntity_captured.getFirstGameSide().getTurnScoreEntityList().forEach(
                            turn -> {
                                Assertions.assertEquals(0,turn.getTurnScore());
                                Assertions.assertFalse(turn.getTabled());
                            }
                    );
                }
        );

    }


}
