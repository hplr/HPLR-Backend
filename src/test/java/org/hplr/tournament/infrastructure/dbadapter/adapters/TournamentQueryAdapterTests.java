package org.hplr.tournament.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.repositories.TournamentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class TournamentQueryAdapterTests {

    private AutoCloseable closeable;
    static final UUID test_tournamentId = UUID.randomUUID();
    static final String test_name = "test";
    static final LocalDateTime test_tournamentStart = LocalDateTime.of(2001,12,25,12,0,0);
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


    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentEntity mock_tournamentEntity;

    @InjectMocks
    private TournamentQueryAdapter tournamentQueryAdapter;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mock_tournamentEntity = new TournamentEntity(
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
    void select_tournament_find_none_and_throw_NoSuchElementException() {
        when(tournamentRepository.findByTournamentId(test_tournamentId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> tournamentQueryAdapter.selectTournamentByTournamentId(test_tournamentId)
        );
    }

    @Test
    void select_tournament_find_one_and_succeed() {
        when(tournamentRepository.findByTournamentId(test_tournamentId))
                .thenReturn(
                        Optional.ofNullable(mock_tournamentEntity)
                );
        Assertions.assertDoesNotThrow(
                () -> {
                    Optional<TournamentSelectDto> result = tournamentQueryAdapter.selectTournamentByTournamentId(test_tournamentId);
                    Assertions.assertNotNull(result);
                    Assertions.assertTrue(result.isPresent());
                }
        );
    }

    @Test
    void find_all_tournaments_by_status_find_none_and_succeed(){
        when(tournamentRepository.findAllByClosed(test_closed_true))
                .thenReturn(List.of());
        List<TournamentSelectDto> tournamentSelectDtoList = tournamentQueryAdapter.selectAllTournaments(test_closed_true);
        Assertions.assertNotNull(tournamentSelectDtoList);
        Assertions.assertTrue(tournamentSelectDtoList.isEmpty());
    }

    @Test
    void find_all_tournaments_by_status_find_one_and_succeed(){
        when(tournamentRepository.findAllByClosed(test_closed_false))
                .thenReturn(List.of());
        when(tournamentRepository.findAllByClosed(test_closed_true))
                .thenReturn(List.of(mock_tournamentEntity));
        List<TournamentSelectDto> tournamentSelectDtoList = tournamentQueryAdapter.selectAllTournaments(test_closed_false);
        Assertions.assertNotNull(tournamentSelectDtoList);
        Assertions.assertTrue(tournamentSelectDtoList.isEmpty());
        tournamentSelectDtoList = tournamentQueryAdapter.selectAllTournaments(test_closed_true);
        Assertions.assertNotNull(tournamentSelectDtoList);
        Assertions.assertFalse(tournamentSelectDtoList.isEmpty());
        Assertions.assertEquals(1, tournamentSelectDtoList.size());
    }
}
