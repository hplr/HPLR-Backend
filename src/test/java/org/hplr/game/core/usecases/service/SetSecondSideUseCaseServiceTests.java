package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.dto.CreatedGameSaveSecondSideDto;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerArmyDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.game.core.usecases.port.out.command.SaveGameSecondSideCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.mappers.PlayerMapper;
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
class SetSecondSideUseCaseServiceTests {

    private AutoCloseable closeable;

    @Mock
    private SelectPlayerByUserIdQueryInterface selectPlayerByUserIdQueryInterface;
    @Mock
    private SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;
    @Mock
    private SaveGameSecondSideCommandInterface saveGameSecondSideCommandInterface;
    @Mock
    private GameSelectDto mock_gameSelectDto;
    @Mock
    private CreatedGameSaveSecondSideDto mock_createdGameSaveSecondSideDto;

    @InjectMocks
    SetSecondSideUseCaseService setSecondSideUseCaseService;

    @Captor
    ArgumentCaptor<GameSnapshot> gameSnapshotArgumentCaptor;

    static final UUID test_gameId = UUID.randomUUID();
    static final UUID test_gameSideId = UUID.randomUUID();
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
        ));

    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void set_second_side_find_no_game_and_throw_NoSuchElementException(){
        when(selectGameByGameIdQueryInterface.selectGameByGameId(test_gameId))
                .thenReturn(Optional.empty());
        when(mock_createdGameSaveSecondSideDto.gameId()).thenReturn(UUID.randomUUID());
        Assertions.assertThrows(NoSuchElementException.class,
                ()-> setSecondSideUseCaseService.setSecondSideForGame(mock_createdGameSaveSecondSideDto)
                );
    }

    @Test
    void set_second_side_find_game_fail_validation_and_throw_HPLRValidationException(){
        mock_createdGameSaveSecondSideDto = new CreatedGameSaveSecondSideDto(
                test_gameId,
                Allegiance.LOYALIST,
                List.of(
                        new InitialGameSidePlayerDataDto(
                                test_playerId_second,
                                new InitialGameSidePlayerArmyDto(
                                        "IH",
                                        "IH",
                                        -400L
                                ),
                                new ArrayList<>()
                        )
                )
        );
        PlayerEntity player = new PlayerEntity(
                test_playerId_second,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        when(selectGameByGameIdQueryInterface.selectGameByGameId(test_gameId))
                .thenReturn(Optional.of(mock_gameSelectDto));
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId_second))
                .thenReturn(Optional.of(PlayerMapper.toDto(player)));
        Assertions.assertThrows(HPLRValidationException.class,
                ()-> setSecondSideUseCaseService.setSecondSideForGame(mock_createdGameSaveSecondSideDto)
        );
        verify(selectPlayerByUserIdQueryInterface, times(1)).selectPlayerByUserId(test_playerId_second);
        verify(selectGameByGameIdQueryInterface, times(1)).selectGameByGameId(test_gameId);
        verify(saveGameSecondSideCommandInterface,times(0)).saveGameSecondSide(any());

    }


    @Test
    void set_second_side_find_game_and_succeed(){
        PlayerEntity player = new PlayerEntity(
                test_playerId_second,
                test_name,
                test_email,
                test_pwHash,
                test_registrationTime,
                test_lastLogin,
                test_nickname,
                test_motto,
                test_score);
        mock_createdGameSaveSecondSideDto = new CreatedGameSaveSecondSideDto(
                test_gameId,
                Allegiance.LOYALIST,
                List.of(
                        new InitialGameSidePlayerDataDto(
                                test_playerId_second,
                                new InitialGameSidePlayerArmyDto(
                                        "IH",
                                        "IH",
                                        1L
                                ),
                                new ArrayList<>()
                        )
                )
        );
        Assertions.assertDoesNotThrow(
                ()->{
                    when(selectGameByGameIdQueryInterface.selectGameByGameId(test_gameId))
                            .thenReturn(Optional.of(mock_gameSelectDto));
                    when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId_second))
                            .thenReturn(Optional.of(PlayerMapper.toDto(player)));
                    UUID result = setSecondSideUseCaseService.setSecondSideForGame(mock_createdGameSaveSecondSideDto);
                    Assertions.assertNotNull(result);
                    verify(selectPlayerByUserIdQueryInterface, times(1)).selectPlayerByUserId(test_playerId_second);
                    verify(selectGameByGameIdQueryInterface, times(1)).selectGameByGameId(test_gameId);
                    verify(saveGameSecondSideCommandInterface).saveGameSecondSide(gameSnapshotArgumentCaptor.capture());
                    verify(saveGameSecondSideCommandInterface,times(1)).saveGameSecondSide(any());
                    GameSnapshot gameSnapshotCaptured = gameSnapshotArgumentCaptor.getValue();
                    Assertions.assertNotNull(gameSnapshotCaptured.secondGameSide());
                    Assertions.assertEquals(Status.AWAITING, gameSnapshotCaptured.gameStatus());
                    gameSnapshotCaptured.secondGameSide().gameSidePlayerDataList().forEach(
                            Assertions::assertNotNull
                    );

                }
        );
    }
}
