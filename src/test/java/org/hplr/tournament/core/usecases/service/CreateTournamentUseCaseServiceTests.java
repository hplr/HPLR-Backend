package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.location.core.usecases.port.dto.LocationSaveDto;
import org.hplr.tournament.core.usecases.port.out.command.SaveTournamentCommandInterface;
import org.hplr.tournament.core.usecases.service.dto.InitialTournamentSaveDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class CreateTournamentUseCaseServiceTests {

    private AutoCloseable closeable;
    static final String test_name = "test";
    static final LocalDateTime test_tournamentStart = LocalDateTime.of(2025, 12, 25, 12, 0, 0);
    static final Long test_pointSize = 100L;
    static final Integer test_gameLength = 1;
    static final Integer test_gameTurnAmount = 2;
    static final Integer test_maxPlayers = 4;
    static final String test_country = "Poland";
    static final String test_city = "Łódź";
    static final String test_street = "Duża";
    static final String test_houseNumber = "23";

    @Mock
    private SaveTournamentCommandInterface saveTournamentCommandInterface;

    @Mock
    private InitialTournamentSaveDto mock_tnitialTournamentSaveDto;

    @InjectMocks
    private CreateTournamentUseCaseService createTournamentUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        mock_tnitialTournamentSaveDto = new InitialTournamentSaveDto(
                test_name,
                test_tournamentStart,
                test_pointSize,
                test_gameLength,
                test_gameTurnAmount,
                test_maxPlayers,
                new LocationSaveDto(
                        test_name,
                        test_country,
                        test_city,
                        test_street,
                        test_houseNumber,
                        false
                )
        );

    }


    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void create_tournament_and_succeed(){
        Assertions.assertDoesNotThrow(
                ()->{
                    UUID result = createTournamentUseCaseService.createTournament(mock_tnitialTournamentSaveDto);
                    Assertions.assertNotNull(result);
                    verify(saveTournamentCommandInterface,times(1)).saveTournament(any());
                }
        );
       }
}
