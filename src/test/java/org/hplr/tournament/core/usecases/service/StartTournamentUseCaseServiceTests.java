package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameDeploymentsQueryInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameMissionsQueryInterface;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GamePlayerDataEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GameSideEntity;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.tournament.core.model.TournamentRound;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.out.command.StartTournamentCommandInterface;
import org.hplr.tournament.core.usecases.port.out.query.SelectTournamentByTournamentIdQueryInterface;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.mappers.TournamentDatabaseMapper;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class StartTournamentUseCaseServiceTests {

    private AutoCloseable closeable;
    static final UUID test_tournamentId = UUID.randomUUID();
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
    static final UUID test_playerId = UUID.randomUUID();
    static final UUID test_playerId_second = UUID.randomUUID();
    static final UUID test_playerId_third = UUID.randomUUID();
    static final UUID test_playerId_fourth = UUID.randomUUID();
    static final Allegiance test_allegiance = Allegiance.LOYALIST;
    static final String test_armyType = "type_test";
    static final String test_armyType_second = "type_test_second";
    static final String test_armyType_third = "type_test_third";
    static final String test_armyType_fourth = "type_test_fourth";
    static final String test_armyName = "name_test";
    static final Long test_armyPointSize = 1L;
    static final String test_player_name = "John";
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;
    static final UUID test_sideId = UUID.randomUUID();
    static final UUID test_sideId_second = UUID.randomUUID();
    static final UUID test_sideId_third = UUID.randomUUID();
    static final UUID test_sideId_fourth = UUID.randomUUID();
    static final Boolean test_first = false;
    static final String test_mission_name = "test_mission";
    static final String test_deployment_name = "test_deployment";


    @Mock
    private SelectTournamentByTournamentIdQueryInterface selectTournamentByTournamentIdQueryInterface;
    @Mock
    private SelectAllGameDeploymentsQueryInterface selectAllGameDeploymentsQueryInterface;
    @Mock
    private SelectAllGameMissionsQueryInterface selectAllGameMissionsQueryInterface;
    @Mock
    private StartTournamentCommandInterface startTournamentCommandInterface;

    @Mock
    private TournamentEntity mock_tournamentEntity;
    @InjectMocks
    private StartTournamentUseCaseService startTournamentUseCaseService;

    @Captor
    private ArgumentCaptor<TournamentSnapshot> tournamentSnapshotArgumentCaptor;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mock_tournamentEntity = new TournamentEntity(
                null,
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
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void start_tournament_find_none_and_throw_NoSuchElementException() {
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> startTournamentUseCaseService.startTournament(test_tournamentId)
        );
    }

    @Test
    void start_tournament_find_one_fail_validation_and_throw_HPLRValidationException() {
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(TournamentDatabaseMapper.fromEntity(mock_tournamentEntity)));
        Assertions.assertThrows(HPLRValidationException.class,
                () -> startTournamentUseCaseService.startTournament(test_tournamentId)
        );
    }

    @Test
    void start_tournament_find_one_and_succeed() {
        List<GameSideEntity> gameSideEntityList = List.of(
                new GameSideEntity(
                        null,
                        test_sideId,
                        test_allegiance,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_playerId,
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
                                                        test_armyType
                                                ),
                                                test_armyName,
                                                test_armyPointSize
                                        ),
                                        new ArrayList<>()
                                )
                        ),
                        test_first,
                        List.of()
                ),
                new GameSideEntity(
                        null,
                        test_sideId_second,
                        test_allegiance,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_playerId_second,
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
                                                        test_armyType_second
                                                ),
                                                test_armyName,
                                                test_armyPointSize
                                        ),
                                        new ArrayList<>()
                                )
                        ),
                        test_first,
                        List.of()
                ),
                new GameSideEntity(
                        null,
                        test_sideId_third,
                        test_allegiance,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_playerId_third,
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
                                                        test_armyType_third
                                                ),
                                                test_armyName,
                                                test_armyPointSize
                                        ),
                                        new ArrayList<>()
                                )
                        ),
                        test_first,
                        List.of()
                ),
                new GameSideEntity(
                        null,
                        test_sideId_fourth,
                        test_allegiance,
                        List.of(
                                new GamePlayerDataEntity(
                                        null,
                                        new PlayerEntity(
                                                test_playerId_fourth,
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
                                                        test_armyType_fourth
                                                ),
                                                test_armyName,
                                                test_armyPointSize
                                        ),
                                        new ArrayList<>()
                                )
                        ),
                        test_first,
                        List.of()
                )

        );
        mock_tournamentEntity = new TournamentEntity(
                null,
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
                gameSideEntityList,
                test_closed_false
        );
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(TournamentDatabaseMapper.fromEntity(mock_tournamentEntity)));
        when(selectAllGameMissionsQueryInterface.getAllGameMissions()).
                thenReturn(List.of(new GameMission(test_mission_name)));
        when(selectAllGameDeploymentsQueryInterface.getAllGameDeployments()).
                thenReturn(List.of(new GameDeployment(test_deployment_name)));
        Assertions.assertDoesNotThrow(
                () -> {
                    UUID result = startTournamentUseCaseService.startTournament(test_tournamentId);
                    verify(startTournamentCommandInterface, times(1)).startTournament(tournamentSnapshotArgumentCaptor.capture());
                    Assertions.assertNotNull(result);
                    Assertions.assertEquals(test_tournamentId, result);
                    TournamentSnapshot tournamentSnapshot = tournamentSnapshotArgumentCaptor.getValue();
                    Assertions.assertNotNull(tournamentSnapshot);
                    Assertions.assertTrue(tournamentSnapshot.closed());
                    Assertions.assertNotNull(tournamentSnapshot.tournamentRoundList());
                    Assertions.assertFalse(tournamentSnapshot.tournamentRoundList().isEmpty());
                    for (int i = 0; i < 3; i++) {
                        TournamentRound round = tournamentSnapshot.tournamentRoundList().get(i);
                        Assertions.assertNotNull(round.getGameList());
                        Assertions.assertFalse(round.getGameList().isEmpty());
                        int finalI = i;
                        round.getGameList().forEach(game -> {
                            Assertions.assertNotNull(game.getGameData().gameStartTime());
                            Assertions.assertEquals(
                                    test_tournamentStart.plusHours((long) tournamentSnapshot.tournamentData().gameLength() * finalI),
                                    game.getGameData().gameStartTime()
                            );
                            Assertions.assertNotNull(game.getGameData().gameEndTime());
                            Assertions.assertEquals(
                                    test_tournamentStart.plusHours((long) tournamentSnapshot.tournamentData().gameLength() *  (1 + finalI)),
                                    game.getGameData().gameEndTime()
                            );
                        });
                    }

                }
        );
    }
}
