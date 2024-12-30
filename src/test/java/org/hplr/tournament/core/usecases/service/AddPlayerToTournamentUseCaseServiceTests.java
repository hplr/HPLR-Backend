package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerArmyDto;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GamePlayerDataEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GameSideEntity;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.tournament.core.usecases.port.out.command.UpdateTournamentQueryInterface;
import org.hplr.tournament.core.usecases.port.out.query.SelectTournamentByTournamentIdQueryInterface;
import org.hplr.tournament.core.usecases.service.dto.AddPlayerToTournamentDto;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.mappers.TournamentDatabaseMapper;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class AddPlayerToTournamentUseCaseServiceTests {

    private AutoCloseable closeable;
    static final UUID test_playerId = UUID.randomUUID();
    static final UUID test_tournamentId = UUID.randomUUID();
    static final Allegiance test_allegiance = Allegiance.LOYALIST;
    static final String test_armyType = "type_test";
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
    static final String test_name = "test";
    static final LocalDateTime test_tournamentStart = LocalDateTime.of(2001, 12, 25, 12, 0, 0);
    static final Long test_pointSize = 100L;
    static final Integer test_gameLength = 1;
    static final Integer test_gameTurnAmount = 2;
    static final Integer test_maxPlayers = 4;
    static final Boolean test_closed_true = true;
    static final String test_country = "Poland";
    static final String test_city = "Łódź";
    static final String test_street = "Duża";
    static final String test_houseNumber = "23";
    static final Double test_latitude = 23.0;
    static final Double test_longitude = 23.0;
    static final UUID test_sideId = UUID.randomUUID();
    @Mock
    private SelectPlayerByUserIdQueryInterface selectPlayerByUserIdQueryInterface;

    @Mock
    private SelectTournamentByTournamentIdQueryInterface selectTournamentByTournamentIdQueryInterface;

    @Mock
    private AddPlayerToTournamentDto mock_addPlayerToTournamentDto;

    @Mock
    private UpdateTournamentQueryInterface updateTournamentQueryInterface;

    @Mock
    private PlayerSelectDto mock_playerSelectDto;

    @Mock
    private TournamentEntity mock_tournamentEntity;

    @InjectMocks
    private AddPlayerToTournamentUseCaseService addPlayerToTournamentUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mock_addPlayerToTournamentDto = new AddPlayerToTournamentDto(
                test_tournamentId,
                test_playerId,
                test_allegiance,
                new InitialGameSidePlayerArmyDto(
                        test_armyType,
                        test_armyName,
                        test_armyPointSize
                ),
                new ArrayList<>()
        );
        mock_playerSelectDto = new PlayerSelectDto(
                test_playerId,
                test_player_name,
                test_nickname,
                test_email,
                test_motto,
                test_score,
                test_pwHash,
                test_registrationTime,
                test_lastLogin);
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
    void add_player_not_find_player_and_throw_NoSuchElementException() {
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                addPlayerToTournamentUseCaseService.addPlayerToTournament(mock_addPlayerToTournamentDto));
    }

    @Test
    void add_player_not_find_tournament_and_throw_NoSuchElementException() {
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId))
                .thenReturn(Optional.of(mock_playerSelectDto));
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                addPlayerToTournamentUseCaseService.addPlayerToTournament(mock_addPlayerToTournamentDto));
    }

    @Test
    void add_player_with_wrong_point_size_and_throw_HPLRValidationException() {
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId))
                .thenReturn(Optional.of(mock_playerSelectDto));
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
                List.of(new GameSideEntity(
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
                        test_closed_true,
                        new ArrayList<>()

                )),
                test_closed_true
        );
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(TournamentDatabaseMapper.fromEntity(mock_tournamentEntity)));
        Assertions.assertThrows(HPLRValidationException.class, () ->
                addPlayerToTournamentUseCaseService.addPlayerToTournament(mock_addPlayerToTournamentDto));
    }

    @Test
    void add_player_that_is_in_tournament_and_throw_HPLRValidationException() {
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId))
                .thenReturn(Optional.of(mock_playerSelectDto));
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(TournamentDatabaseMapper.fromEntity(mock_tournamentEntity)));
        mock_addPlayerToTournamentDto = new AddPlayerToTournamentDto(
                test_tournamentId,
                test_playerId,
                test_allegiance,
                new InitialGameSidePlayerArmyDto(
                        test_armyType,
                        test_armyName,
                        -test_armyPointSize
                ),
                new ArrayList<>()
        );
        Assertions.assertThrows(HPLRValidationException.class, () ->
                addPlayerToTournamentUseCaseService.addPlayerToTournament(mock_addPlayerToTournamentDto));
    }


    @Test
    void add_player_and_succeed() {
        when(selectPlayerByUserIdQueryInterface.selectPlayerByUserId(test_playerId))
                .thenReturn(Optional.of(mock_playerSelectDto));
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(TournamentDatabaseMapper.fromEntity(mock_tournamentEntity)));
        Assertions.assertDoesNotThrow(
                ()->{
                    UUID result = addPlayerToTournamentUseCaseService.addPlayerToTournament(mock_addPlayerToTournamentDto);
                    Assertions.assertNotNull(result);
                    Assertions.assertEquals(test_tournamentId, result);
                    verify(updateTournamentQueryInterface, times(1)).updateTournament(any());
                }
        );
    }
}
