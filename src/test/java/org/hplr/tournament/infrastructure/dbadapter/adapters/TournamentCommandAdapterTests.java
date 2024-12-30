package org.hplr.tournament.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.vo.GameArmy;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.game.infrastructure.dbadapter.repositories.GameArmyTypeRepository;
import org.hplr.game.infrastructure.dbadapter.repositories.GameDeploymentRepository;
import org.hplr.game.infrastructure.dbadapter.repositories.GameMissionRepository;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.location.core.model.Location;
import org.hplr.location.core.usecases.port.dto.LocationSelectDto;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.location.infrastructure.dbadapter.repositories.LocationRepository;
import org.hplr.tournament.core.model.TournamentRound;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.model.vo.TournamentData;
import org.hplr.tournament.core.model.vo.TournamentId;
import org.hplr.tournament.core.model.vo.TournamentLocation;
import org.hplr.tournament.core.model.vo.TournamentPlayer;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.repositories.TournamentRepository;
import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class TournamentCommandAdapterTests {

    private AutoCloseable closeable;
    static final UUID test_tournamentId = UUID.randomUUID();
    static final Long test_id = 100L;
    static final String test_name = "test";
    static final LocalDateTime test_tournamentStart = LocalDateTime.of(2001, 12, 25, 12, 0, 0);
    static final Long test_pointSize = 100L;
    static final Integer test_gameLength = 1;
    static final Integer test_gameTurnAmount = 2;
    static final Integer test_maxPlayers = 4;
    static final Boolean test_closed_true = true;
    static final Boolean test_closed_false = false;
    static final String test_country = "Poland";
    static final String test_city = "Łódź";
    static final String test_street = "Duża";
    static final String test_houseNumber = "23";
    static final Double test_latitude = 23.0;
    static final Double test_longitude = 23.0;
    static final String test_player_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;
    static final String test_armyName = "IH";
    static final UUID test_gameId = UUID.randomUUID();
    static final UUID test_gameSideId = UUID.randomUUID();
    static final UUID test_gameSideId_second = UUID.randomUUID();
    static final UUID test_playerId = UUID.randomUUID();
    static final UUID test_playerId_second = UUID.randomUUID();
    static final String test_mission = "mission";
    static final String test_deployment = "deployment";
    static final Long test_size = 1250L;
    static final Integer test_turnLength = 2;
    static final Integer test_timeLength = 1;
    static final LocalDateTime test_gameStart = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_gameEnd = LocalDateTime.of(2001, 12, 21, 12, 2, 21);

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private PlayerQueryRepository playerQueryRepository;
    @Mock
    private GameArmyTypeRepository gameArmyTypeRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private GameMissionRepository gameMissionRepository;
    @Mock
    private GameDeploymentRepository gameDeploymentRepository;
    @Mock
    private TournamentEntity mock_tournamentEntity;

    @Mock
    private TournamentSnapshot mock_tournamentSnapshot;

    @Mock
    private PlayerEntity mock_playerEntity;

    @Mock
    private PlayerEntity mock_playerEntity_second;

    @Mock
    private GameArmyTypeEntity mock_gameArmyTypeEntity;

    @InjectMocks
    @Spy
    private TournamentCommandAdapter tournamentCommandAdapter;

    @Captor
    private ArgumentCaptor<TournamentEntity> tournamentEntityArgumentCaptor;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mock_tournamentEntity = new TournamentEntity(
                test_id,
                test_tournamentId,
                test_name,
                test_tournamentStart,
                test_pointSize,
                test_gameLength,
                test_gameTurnAmount,
                test_maxPlayers,
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
                new ArrayList<>(),
                new ArrayList<>(),
                test_closed_true
        );
        mock_tournamentSnapshot = new TournamentSnapshot(
                new TournamentId(test_tournamentId),
                new TournamentData(
                        test_name,
                        test_tournamentStart,
                        test_pointSize,
                        test_gameLength,
                        test_gameTurnAmount,
                        test_maxPlayers
                ),
                new TournamentLocation(
                        Location.fromDto(
                                new LocationSelectDto(
                                        UUID.randomUUID(),
                                        test_name,
                                        false,
                                        test_country,
                                        test_city,
                                        test_street,
                                        test_houseNumber,
                                        test_longitude,
                                        test_latitude
                                ))
                ),
                new ArrayList<>(),
                List.of(new TournamentPlayer(
                        Allegiance.LOYALIST,
                        new GameSidePlayerData(
                                Player.fromDto(
                                        new PlayerSelectDto(
                                                test_playerId,
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
                                        new GameArmyType(test_armyName),
                                        test_armyName,
                                        10L
                                ),
                                List.of(
                                        new GameArmy(
                                                new GameArmyType(test_armyName),
                                                test_armyName,
                                                1L
                                        )
                                )
                        )
                )),
                test_closed_false
        );
        mock_playerEntity = new PlayerEntity(
                test_playerId,
                test_player_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score
        );
        mock_playerEntity_second = new PlayerEntity(
                test_playerId_second,
                test_player_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score
        );
        mock_gameArmyTypeEntity = new GameArmyTypeEntity(
                null,
                test_armyName
        );
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_tournament_and_succeed() {
        when(tournamentRepository.findByTournamentId(test_tournamentId))
                .thenReturn(Optional.ofNullable(mock_tournamentEntity));
        tournamentCommandAdapter.saveTournament(mock_tournamentSnapshot);
        verify(tournamentRepository, times(1)).save(any());
    }

    @Test
    void update_tournament_find_none_and_succeed() {
        when(tournamentRepository.findByTournamentId(test_tournamentId))
                .thenReturn(Optional.empty());
        tournamentCommandAdapter.updateTournament(mock_tournamentSnapshot);
        verify(tournamentRepository, times(0)).save(any());
    }

    @Test
    void update_tournament_find_one_and_succeed() {
        when(tournamentRepository.findByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(mock_tournamentEntity));
        when(playerQueryRepository.findByUserId(test_playerId))
                .thenReturn(Optional.of(mock_playerEntity));
        when(gameArmyTypeRepository.findByName(test_armyName))
                .thenReturn(Optional.ofNullable(mock_gameArmyTypeEntity));
        tournamentCommandAdapter.updateTournament(mock_tournamentSnapshot);
        verify(tournamentRepository, times(1)).save(tournamentEntityArgumentCaptor.capture());
        TournamentEntity result = tournamentEntityArgumentCaptor.getValue();
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getGameSideEntityList());
        Assertions.assertFalse(result.getGameSideEntityList().isEmpty());
        Assertions.assertEquals(1, result.getGameSideEntityList().size());
    }

    @Test
    void start_tournament_find_no_players_and_throw_HPLRIllegalStateException() {
        when(playerQueryRepository.findAll())
                .thenReturn(List.of());
        Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> tournamentCommandAdapter.startTournament(mock_tournamentSnapshot)
        );
    }

    @Test
    void start_tournament_find_no_army_types_and_throw_HPLRIllegalStateException() {
        when(playerQueryRepository.findAll())
                .thenReturn(List.of(mock_playerEntity));
        when(gameArmyTypeRepository.findAll())
                .thenReturn(List.of());
        Assertions.assertThrows(HPLRIllegalStateException.class,
                () -> tournamentCommandAdapter.startTournament(mock_tournamentSnapshot)
        );
    }

    @Test
    void start_tournament_find_no_tournament_and_throw_NoSuchElementException() {
        when(playerQueryRepository.findAll())
                .thenReturn(List.of(mock_playerEntity));
        when(gameArmyTypeRepository.findAll())
                .thenReturn(List.of(mock_gameArmyTypeEntity));
        when(tournamentRepository.findByTournamentId(test_tournamentId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> tournamentCommandAdapter.startTournament(mock_tournamentSnapshot)
        );
    }

    @Test
    void start_tournament_find_tournament_and_succeed() {
        GameSelectDto gameSelectDto =
                GameDatabaseMapper.toDto(new GameEntity(
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
                                        test_gameSideId,
                                        Allegiance.LOYALIST,
                                        List.of(
                                                new GamePlayerDataEntity(
                                                        null,
                                                        mock_playerEntity,
                                                        new GameArmyEntity(
                                                                null,
                                                                mock_gameArmyTypeEntity,
                                                                test_name,
                                                                test_score
                                                        ),
                                                        new ArrayList<>()
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
                                                        mock_playerEntity_second,
                                                        new GameArmyEntity(
                                                                null,
                                                                mock_gameArmyTypeEntity,
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
                        )
                );

        when(playerQueryRepository.findAll())
                .thenReturn(List.of(mock_playerEntity, mock_playerEntity_second));
        when(playerQueryRepository.findByUserId(test_playerId))
                .thenReturn(Optional.of(mock_playerEntity));
        when(playerQueryRepository.findByUserId(test_playerId_second))
                .thenReturn(Optional.of(mock_playerEntity_second));
        when(gameArmyTypeRepository.findAll())
                .thenReturn(List.of(mock_gameArmyTypeEntity));
        when(gameArmyTypeRepository.findByName(test_armyName))
                .thenReturn(Optional.ofNullable(mock_gameArmyTypeEntity));
        when(tournamentRepository.findByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(mock_tournamentEntity));

        mock_tournamentSnapshot = new TournamentSnapshot(
                new TournamentId(test_tournamentId),
                new TournamentData(
                        test_name,
                        test_tournamentStart,
                        test_pointSize,
                        test_gameLength,
                        test_gameTurnAmount,
                        test_maxPlayers
                ),
                new TournamentLocation(
                        Location.fromDto(
                                new LocationSelectDto(
                                        UUID.randomUUID(),
                                        test_name,
                                        false,
                                        test_country,
                                        test_city,
                                        test_street,
                                        test_houseNumber,
                                        test_longitude,
                                        test_latitude
                                ))
                ),
                List.of(
                        new TournamentRound(
                                List.of(
                                        Game.fromDto(gameSelectDto)
                                )
                        )
                ),
                List.of(
                        new TournamentPlayer(
                                Allegiance.LOYALIST,
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_playerId,
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
                                                new GameArmyType(test_armyName),
                                                test_armyName,
                                                10L
                                        ),
                                        List.of(
                                                new GameArmy(
                                                        new GameArmyType(test_armyName),
                                                        test_armyName,
                                                        1L
                                                )
                                        )
                                )
                        ),
                        new TournamentPlayer(
                                Allegiance.LOYALIST,
                                new GameSidePlayerData(
                                        Player.fromDto(
                                                new PlayerSelectDto(
                                                        test_playerId_second,
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
                                                new GameArmyType(test_armyName),
                                                test_armyName,
                                                10L
                                        ),
                                        List.of(
                                                new GameArmy(
                                                        new GameArmyType(test_armyName),
                                                        test_armyName,
                                                        1L
                                                )
                                        )
                                )

                        )
                ),
                test_closed_true
        );
        Assertions.assertDoesNotThrow(
                () -> {
                    UUID result = tournamentCommandAdapter.startTournament(mock_tournamentSnapshot);
                    Assertions.assertNotNull(result);
                    Assertions.assertEquals(test_tournamentId, result);
                    verify(tournamentRepository, times(1)).save(tournamentEntityArgumentCaptor.capture());
                    TournamentEntity tournamentEntityCaptured = tournamentEntityArgumentCaptor.getValue();
                    Assertions.assertNotNull(tournamentEntityCaptured);
                    Assertions.assertEquals(test_id, tournamentEntityCaptured.getId());
                    Assertions.assertNotNull(tournamentEntityCaptured.getTournamentRoundEntityList());
                    Assertions.assertFalse(tournamentEntityCaptured.getTournamentRoundEntityList().isEmpty());
                    tournamentEntityCaptured.getTournamentRoundEntityList().forEach(round ->
                    {
                        Assertions.assertNotNull(round.getGameEntityList());
                        Assertions.assertFalse(round.getGameEntityList().isEmpty());
                        round.getGameEntityList().forEach(gameEntity -> {
                            Assertions.assertNotNull(gameEntity.getLocationEntity());
                            Assertions.assertNotNull(gameEntity.getGameMissionEntity());
                            Assertions.assertNotNull(gameEntity.getGameDeploymentEntity());
                            Assertions.assertNotNull(gameEntity.getFirstGameSide());
                            Assertions.assertNotNull(gameEntity.getSecondGameSide());
                        });
                    }
                    );
                    tournamentEntityCaptured.getGameSideEntityList().forEach(gameSideEntity ->
                    {
                        Assertions.assertNotNull(gameSideEntity.getGamePlayerDataEntityList());
                        Assertions.assertEquals(1, gameSideEntity.getGamePlayerDataEntityList().size());
                        gameSideEntity.getGamePlayerDataEntityList().forEach(gamePlayerDataEntity -> {
                            Assertions.assertNotNull(gamePlayerDataEntity.getAllyArmyEntityList());
                            Assertions.assertFalse(gamePlayerDataEntity.getAllyArmyEntityList().isEmpty());
                            gamePlayerDataEntity.getAllyArmyEntityList().forEach(allyArmyEntity -> {
                                Assertions.assertNotNull(allyArmyEntity.getGameArmyTypeEntity());
                                Assertions.assertNotNull(allyArmyEntity.getName());
                                Assertions.assertNotNull(allyArmyEntity.getPointValue());
                            });
                        });
                    });
                    mock_tournamentSnapshot.playerList().forEach(player->
                    {
                        Optional<GameSideEntity> gameSideEntityOptional =
                                tournamentEntityCaptured.getGameSideEntityList()
                                        .stream()
                                        .filter(gameSideEntity -> gameSideEntity.getGamePlayerDataEntityList()
                                                .stream()
                                                .anyMatch(gamePlayerDataEntity -> gamePlayerDataEntity.getPlayerEntity().getUserId().equals(player.gameSidePlayerData().player().getUserId().id()))
                                        )
                                        .findFirst();
                        Assertions.assertTrue(gameSideEntityOptional.isPresent());
                    });
                }
        );
    }
}
