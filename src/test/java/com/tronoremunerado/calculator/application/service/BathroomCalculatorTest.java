package com.tronoremunerado.calculator.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class BathroomCalculatorTest {

    private BathroomCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new BathroomCalculator();
    }

    @Nested
    @DisplayName("Calculate Daily Percentage of Shift Tests")
    class CalculateDailyPercentageOfShiftTests {

        @Test
        @DisplayName("Should calculate 0% when no bathroom visits")
        void shouldCalculateZeroPercentageWhenNoBathroomVisits() {
            // given
            int averageBathroomTime = 10;
            int numberOfVisitsPerDay = 0;
            int minutesWorked = 480; // 8 hours

            // when
            double percentage = calculator.calculateDailyPercentageOfShift(
                averageBathroomTime,
                numberOfVisitsPerDay,
                minutesWorked
            );

            // then
            assertThat(percentage).isZero();
        }

        @Test
        @DisplayName("Should calculate 100% when bathroom time equals shift time")
        void shouldCalculateFullPercentageWhenBathroomTimeEqualsShiftTime() {
            // given
            int averageBathroomTime = 60;
            int numberOfVisitsPerDay = 8;
            int minutesWorked = 480; // 8 hours

            // when
            double percentage = calculator.calculateDailyPercentageOfShift(
                averageBathroomTime,
                numberOfVisitsPerDay,
                minutesWorked
            );

            // then
            assertThat(percentage).isEqualTo(100.0);
        }

        @ParameterizedTest
        @CsvSource({
            "10, 3, 480, 6.25",  // 30 minutes in bathroom during 8 hour shift
            "5, 4, 480, 4.17",   // 20 minutes in bathroom during 8 hour shift
            "15, 2, 480, 6.25",  // 30 minutes in bathroom during 8 hour shift
            "7, 5, 360, 9.72"    // 35 minutes in bathroom during 6 hour shift
        })
        @DisplayName("Should calculate correct percentage for various scenarios")
        void shouldCalculateCorrectPercentageForVariousScenarios(
            int averageBathroomTime,
            int numberOfVisitsPerDay,
            int minutesWorked,
            double expectedPercentage
        ) {
            // when
            double percentage = calculator.calculateDailyPercentageOfShift(
                averageBathroomTime,
                numberOfVisitsPerDay,
                minutesWorked
            );

            // then
            assertThat(percentage).isCloseTo(expectedPercentage, offset(0.01));
        }
    }
}
