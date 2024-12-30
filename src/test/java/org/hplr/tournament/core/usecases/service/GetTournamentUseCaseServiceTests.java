package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.usecases.port.out.query.SelectTournamentByTournamentIdQueryInterface;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.mappers.TournamentDatabaseMapper;
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
class GetTournamentUseCaseServiceTests {

    private AutoCloseable closeable;
    static final UUID test_tournamentId = UUID.randomUUID();
    static final String test_name = "test";
    static final LocalDateTime test_tournamentStart = LocalDateTime.of(2001,12,25,12,0,0);
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
    @Mock
    private SelectTournamentByTournamentIdQueryInterface selectTournamentByTournamentIdQueryInterface;

    @Mock
    private TournamentEntity mock_tournamentEntity;

    @InjectMocks
    private GetTournamentUseCaseService getTournamentUseCaseService;

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
    void fetch_tournament_find_none_and_throw_NoSuchElementException(){
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () ->  getTournamentUseCaseService.getTournament(test_tournamentId)
                );
        verify(selectTournamentByTournamentIdQueryInterface, times(1)).selectTournamentByTournamentId(test_tournamentId);
    }

    @Test
    void fetch_tournament_find_one_and_succeed(){
        when(selectTournamentByTournamentIdQueryInterface.selectTournamentByTournamentId(test_tournamentId))
                .thenReturn(Optional.of(TournamentDatabaseMapper.fromEntity(mock_tournamentEntity)));
        Assertions.assertDoesNotThrow(
                ()->{
                    TournamentSnapshot result = getTournamentUseCaseService.getTournament(test_tournamentId);
                    Assertions.assertNotNull(result);
                    verify(selectTournamentByTournamentIdQueryInterface, times(1))
                            .selectTournamentByTournamentId(test_tournamentId);
                }
        );

    }


}
