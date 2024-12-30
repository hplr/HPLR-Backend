package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.elo.core.usecases.service.CalculateAverageELOForGameSideUseCaseService;
import org.hplr.elo.core.usecases.service.CalculateELOChangeForGameUseCaseService;
import org.hplr.elo.core.usecases.service.CalculateScoreForGameUseCaseService;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.out.command.SaveFinishedGameCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class FinishGameManualUseCaseServiceTests {

    private AutoCloseable closeable;
    static final UUID test_gameId = UUID.randomUUID();
    static final UUID test_gameSideId = UUID.randomUUID();
    static final UUID test_gameSideId_second = UUID.randomUUID();
    static final UUID test_playerId = UUID.randomUUID();
    static final UUID test_playerId_second = UUID.randomUUID();
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
    static final Integer test_turnLength = 2;
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

    @Mock
    private SaveFinishedGameCommandInterface saveFinishedGameCommandInterface;

    @Spy
    private final CalculateELOChangeForGameUseCaseService calculateELOChangeForGameUseCaseService
            = new CalculateELOChangeForGameUseCaseService();
    @Spy
    private final CalculateAverageELOForGameSideUseCaseService calculateAverageELOForGameSideUseCaseService
            = new CalculateAverageELOForGameSideUseCaseService();
    @Spy
    private final CalculateScoreForGameUseCaseService calculateScoreForGameUseCaseService
            = new CalculateScoreForGameUseCaseService();

    @Mock
    GameSelectDto mock_gameSelectDto;

    @InjectMocks
    private FinishGameManualUseCaseService finishGameManualUseCaseService;

    @Captor
    private ArgumentCaptor<GameSnapshot> gameSnapshotArgumentCaptor;

    @Captor
    private ArgumentCaptor<List<Long>> eloArgumentCaptor;

    private Map<Integer, Long> eloMap;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mock_gameSelectDto = GameDatabaseMapper.toDto(new GameEntity(
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
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void finish_game_find_no_game_and_throw_NoSuchElementException(){
        when(selectGameByGameIdQueryInterface.selectGameByGameId(test_gameId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                ()-> finishGameManualUseCaseService.finishGame(test_gameId)
                );
        verify(calculateAverageELOForGameSideUseCaseService, times(0)).calculateAverageELO(any());
        verify(calculateScoreForGameUseCaseService, times(0)).calculateScoreForGame(anyList(), anyList());
        verify(calculateELOChangeForGameUseCaseService, times(0)).calculateChangeForGame(anyLong(), anyLong(), anyLong());
    }

    @Test
    void finish_game_find_game_and_throw_HPLRValidationException(){
        when(selectGameByGameIdQueryInterface.selectGameByGameId(test_gameId))
                .thenReturn(Optional.of(mock_gameSelectDto));
        Assertions.assertThrows(HPLRValidationException.class,
                ()-> finishGameManualUseCaseService.finishGame(test_gameId)
        );
        verify(calculateAverageELOForGameSideUseCaseService, times(0)).calculateAverageELO(any());
        verify(calculateScoreForGameUseCaseService, times(0)).calculateScoreForGame(anyList(), anyList());
        verify(calculateELOChangeForGameUseCaseService, times(0)).calculateChangeForGame(anyLong(), anyLong(), anyLong());
    }

    @Test
    void finish_game_find_game_and_succeed(){
        mock_gameSelectDto = GameDatabaseMapper.toDto(new GameEntity(
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
                                        1L,
                                        false
                                ),
                                new GameTurnScoreEntity(
                                        null,
                                        2L,
                                        1L,
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
                                        1L,
                                        false
                                ),
                                new GameTurnScoreEntity(
                                        null,
                                        2L,
                                        2L,
                                        false
                                )
                        )
                )
        ));
        when(selectGameByGameIdQueryInterface.selectGameByGameId(test_gameId))
                .thenReturn(Optional.of(mock_gameSelectDto));
        Assertions.assertDoesNotThrow(
                ()-> {
                    UUID result = finishGameManualUseCaseService.finishGame(test_gameId);
                    Assertions.assertNotNull(result);
                    Assertions.assertEquals(test_gameId, result);
                }
        );
        verify(selectGameByGameIdQueryInterface,times(1)).selectGameByGameId(test_gameId);
        verify(saveFinishedGameCommandInterface,times(1)).saveFinishedGame(any(), any());
        verify(saveFinishedGameCommandInterface).saveFinishedGame(gameSnapshotArgumentCaptor.capture(), any());
        verify(calculateAverageELOForGameSideUseCaseService, times(2)).calculateAverageELO(eloArgumentCaptor.capture());
        List<List<Long>> eloLists = eloArgumentCaptor.getAllValues();
        eloLists.forEach(eloList -> eloList.forEach(
                elo -> Assertions.assertNotEquals(0L, elo)
        ));
        verify(calculateScoreForGameUseCaseService, times(1)).calculateScoreForGame(anyList(), anyList());
        verify(calculateELOChangeForGameUseCaseService, times(1)).calculateChangeForGame(anyLong(), anyLong(), anyLong());
        eloMap = calculateELOChangeForGameUseCaseService.calculateChangeForGame(test_score, test_score, -1L);
        GameSnapshot gameSnapshot = gameSnapshotArgumentCaptor.getValue();
        Assertions.assertNotNull(gameSnapshot);
        Assertions.assertEquals(Status.FINISHED, gameSnapshot.gameStatus());
        gameSnapshot.firstGameSide().gameSidePlayerDataList().forEach(
                player -> Assertions.assertEquals(test_score,player.player().playerRanking().score()-eloMap.get(1))
        );
        gameSnapshot.secondGameSide().gameSidePlayerDataList().forEach(
                player -> Assertions.assertEquals(test_score,player.player().playerRanking().score()+eloMap.get(1))
        );
    }

}
