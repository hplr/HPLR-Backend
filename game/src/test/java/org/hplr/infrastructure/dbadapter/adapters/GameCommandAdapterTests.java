package org.hplr.infrastructure.dbadapter.adapters;

import org.hplr.core.enums.Allegiance;
import org.hplr.core.enums.Status;
import org.hplr.core.model.GameSide;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.model.Location;
import org.hplr.core.model.Player;
import org.hplr.core.model.vo.*;
import org.hplr.core.usecases.port.dto.LocationSaveDto;
import org.hplr.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.exception.HPLRIllegalStateException;
import org.hplr.exception.HPLRValidationException;
import org.hplr.exception.LocationCalculationException;
import org.hplr.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.hplr.infrastructure.dbadapter.entities.GameEntity;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.infrastructure.dbadapter.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

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

    @Mock
    private LocationRepository mock_locationRepository;
    @Mock
    private PlayerRepository mock_playerRepository;
    @Mock
    private GameRepository mock_gameRepository;
    @Mock
    private GameMissionRepository mock_gameMissionRepository;
    @Mock
    private GameDeploymentRepository mock_gameDeploymentRepository;
    @Mock
    private GameArmyTypeRepository mock_gameArmyTypeRepository;
    @Mock
    private GameSnapshot mock_gameSnapshot;

    @InjectMocks
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
        when(mock_playerRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> gameCommandAdapter.saveGame(mock_gameSnapshot)
        );
    }

    @Test
    void save_game_fetch_no_army_types_and_throw_IllegalStateException() {
        when(mock_playerRepository.findAll()).thenReturn(List.of(
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
                new GameSide(
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

                                        new ELO(100L),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null

                                )
                        ),
                        false,
                        test_turnLength),
                new GameSide(
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

                                        new ELO(100L),
                                        new GameArmy(
                                                new GameArmyType("IH"),
                                                test_nickname,
                                                1250L
                                        ),
                                        null

                                )
                        ),
                        true,
                        test_turnLength)
        );

        when(mock_playerRepository.findAll()).thenReturn(List.of(
                test_playerEntity1, test_playerEntity2
        ));
        when(mock_gameArmyTypeRepository.findAll()).thenReturn(
                List.of(test_gameArmyTypeEntity, test_gameArmyType2Entity)
        );
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
    }


}
