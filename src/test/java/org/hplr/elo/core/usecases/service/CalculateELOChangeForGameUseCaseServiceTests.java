package org.hplr.elo.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;

import java.util.Map;

import static org.mockito.MockitoAnnotations.openMocks;

@RequiredArgsConstructor
class CalculateELOChangeForGameUseCaseServiceTests {

    private AutoCloseable closeable;
    static final Long test_first_elo = 1400L;
    static final Long test_second_elo = 1000L;
    static final Long test_game_score_won = 20L;
    static final Long test_game_score_lost = -20L;
    static final Long test_game_score_draw = 0L;
    static final Long test_game_score_wrong = 25L;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @InjectMocks
    private CalculateELOChangeForGameUseCaseService calculateELOChangeForGameUseCaseService;

    @ParameterizedTest
    @ValueSource(longs = {
            20L, 19L, 18L, 17L, 16L, 15L,
            14L, 13L, 12L, 11L, 10L, 9L,
            8L, 7L, 6L, 5L, 4L, 3L,
            2L, 1L
    })
    void get_game_won_for_first_side_and_check_probabilities(Long test_game_score_won) {
        Assertions.assertDoesNotThrow(
                () -> {
                    Map<Integer, Long> probabilities =
                            calculateELOChangeForGameUseCaseService.calculateChangeForGame(test_first_elo, test_second_elo, test_game_score_won);
                    Assertions.assertNotNull(probabilities);
                    Assertions.assertTrue(probabilities.containsKey(1));
                    Assertions.assertTrue(probabilities.containsKey(2));
                    Assertions.assertEquals(1, probabilities.get(1).compareTo(probabilities.get(2)));
                    Long assumed_probability_first = (long) (ConstValues.ELO_CONST * test_game_score_won *
                            (1 - (1 / (1 + Math.pow(10, ((double) (test_second_elo - test_first_elo) / (ConstValues.INITIAL_WEIGHT)))))));
                    Long assumed_probability_second = (long) (ConstValues.ELO_CONST * test_game_score_won *
                            (0 - (1 / (1 + Math.pow(10, ((double) (test_first_elo - test_second_elo) / (ConstValues.INITIAL_WEIGHT)))))));
                    Assertions.assertEquals(probabilities.get(1), assumed_probability_first);
                    Assertions.assertEquals(probabilities.get(2), assumed_probability_second);
                    Assertions.assertTrue(probabilities.get(1) > 0);
                    Assertions.assertTrue(probabilities.get(2) < 0);
                });
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -20L, -19L, -18L, -17L, -16L, -15L,
            -14L, -13L, -12L, -11L, -10L, -9L,
            -8L, -7L, -6L, -5L, -4L, -3L,
            -2L, -1L
    })
    void get_game_lost_for_first_side_and_check_probabilities() {
        Assertions.assertDoesNotThrow(
                () -> {
                    Map<Integer, Long> probabilities =
                            calculateELOChangeForGameUseCaseService.calculateChangeForGame(test_first_elo, test_second_elo, test_game_score_lost);
                    Assertions.assertNotNull(probabilities);
                    Assertions.assertTrue(probabilities.containsKey(1));
                    Assertions.assertTrue(probabilities.containsKey(2));
                    Assertions.assertEquals(-1, probabilities.get(1).compareTo(probabilities.get(2)));
                    Long assumed_probability_first = (long) (ConstValues.ELO_CONST * test_game_score_won *
                            (0 - (1 / (1 + Math.pow(10, ((double) (test_second_elo - test_first_elo) / (ConstValues.INITIAL_WEIGHT)))))));
                    Long assumed_probability_second = (long) (ConstValues.ELO_CONST * test_game_score_won *
                            (1 - (1 / (1 + Math.pow(10, ((double) (test_first_elo - test_second_elo) / (ConstValues.INITIAL_WEIGHT)))))));
                    Assertions.assertEquals(probabilities.get(1), assumed_probability_first);
                    Assertions.assertEquals(probabilities.get(2), assumed_probability_second);
                    Assertions.assertTrue(probabilities.get(1) < 0);
                    Assertions.assertTrue(probabilities.get(2) > 0);
                });
    }

    @Test
    void get_game_draw_and_check_probabilities() {
        Assertions.assertDoesNotThrow(
                () -> {
                    Map<Integer, Long> probabilities =
                            calculateELOChangeForGameUseCaseService.calculateChangeForGame(test_first_elo, test_second_elo, test_game_score_draw);
                    Assertions.assertNotNull(probabilities);
                    Assertions.assertTrue(probabilities.containsKey(1));
                    Assertions.assertTrue(probabilities.containsKey(2));
                    Assertions.assertEquals(0, probabilities.get(1).compareTo(probabilities.get(2)));
                    Long assumed_probability_first = (long) (ConstValues.ELO_CONST * test_game_score_draw *
                            (0.5 - (1 / (1 + Math.pow(10, ((double) (test_second_elo - test_first_elo) / (ConstValues.INITIAL_WEIGHT)))))));
                    Long assumed_probability_second = (long) (ConstValues.ELO_CONST * test_game_score_draw *
                            (0.5 - (1 / (1 + Math.pow(10, ((double) (test_first_elo - test_second_elo) / (ConstValues.INITIAL_WEIGHT)))))));
                    Assertions.assertEquals(probabilities.get(1), assumed_probability_first);
                    Assertions.assertEquals(probabilities.get(2), assumed_probability_second);
                    Assertions.assertEquals(0, (long) probabilities.get(1));
                    Assertions.assertEquals(0, (long) probabilities.get(2));
                });
    }

    @Test
    void get_game_wrong_score_and_throw_HPLRIllegalStateException() {
        Assertions.assertThrows(HPLRIllegalStateException.class,
                ()->
                        calculateELOChangeForGameUseCaseService.calculateChangeForGame(test_first_elo, test_second_elo, test_game_score_wrong)
                );
    }
}
