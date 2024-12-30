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
class CalculateAverageELOForGameSideUseCaseServiceTests {

    private AutoCloseable closeable;

    @InjectMocks
    private CalculateAverageELOForGameSideUseCaseService calculateAverageELOForGameSideUseCaseService;

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
        Assertions.assertNull(calculateAverageELOForGameSideUseCaseService
                .calculateAverageELO(List.of()));
    }

    @Test
    void get_elo_and_succeed(){
        Long sum = calculateAverageELOForGameSideUseCaseService
                .calculateAverageELO(List.of(140L, 120L, 100L, 80L));
        Assertions.assertNotNull(sum);
        Assertions.assertEquals(110L, sum);
    }
}
