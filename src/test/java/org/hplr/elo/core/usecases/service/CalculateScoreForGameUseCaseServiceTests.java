package org.hplr.elo.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class CalculateScoreForGameUseCaseServiceTests {

    private AutoCloseable closeable;

    @InjectMocks
    private CalculateScoreForGameUseCaseService calculateScoreForGameUseCaseService;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void get_no_elo_and_get_null_and_fail(){
        Assertions.assertNull(calculateScoreForGameUseCaseService
                .calculateScoreForGame(List.of(), List.of()));
    }

    @Test
    void get_elo_and_succeed(){
        Long scoreForGame = calculateScoreForGameUseCaseService
                .calculateScoreForGame(List.of(1L, 3L, 10L), List.of(2L, 2L, 4L));
        Assertions.assertNotNull(scoreForGame);
        Assertions.assertEquals(6L, scoreForGame);
    }
}
