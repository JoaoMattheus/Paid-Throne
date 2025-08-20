package com.tronoremunerado.calculator.domain.validation;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MaxTotalBathroomTimeValidatorTest {

    private MaxTotalBathroomTimeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MaxTotalBathroomTimeValidator();
        validator.initialize(null);
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should return true when King is null")
        void shouldReturnTrueWhenKingIsNull() {
            assertThat(validator.isValid(null, null)).isTrue();
        }

        @Test
        @DisplayName("Should return true when total bathroom time is exactly 60 minutes")
        void shouldReturnTrueWhenTotalBathroomTimeIsExactly60Minutes() {
            // given
            King king = new King(
                "KingArthur",
                15,          // 15 minutes per visit
                4,           // 4 visits per day = 60 minutes total
                BigDecimal.valueOf(50000.0),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
            );

            // when
            boolean isValid = validator.isValid(king, null);

            // then
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Should return true when total bathroom time is less than 60 minutes")
        void shouldReturnTrueWhenTotalBathroomTimeIsLessThan60Minutes() {
            // given
            King king = new King(
                "KingRichard",
                10,          // 10 minutes per visit
                5,           // 5 visits per day = 50 minutes total
                BigDecimal.valueOf(45000.0),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
            );

            // when
            boolean isValid = validator.isValid(king, null);

            // then
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("Should return false when total bathroom time exceeds 60 minutes")
        void shouldReturnFalseWhenTotalBathroomTimeExceeds60Minutes() {
            // given
            King king = new King(
                "KingJohn",
                15,          // 15 minutes per visit
                5,           // 5 visits per day = 75 minutes total
                BigDecimal.valueOf(55000.0),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
            );

            // when
            boolean isValid = validator.isValid(king, null);

            // then
            assertThat(isValid).isFalse();
        }

        @ParameterizedTest
        @CsvSource({
            "5, 10, true",     // 50 minutes total - valid
            "10, 7, false",    // 70 minutes total - invalid
            "3, 15, true",     // 45 minutes total - valid
            "20, 4, false",    // 80 minutes total - invalid
            "30, 3, false"     // 90 minutes total - invalid
        })
        @DisplayName("Should validate various bathroom time scenarios")
        void shouldValidateVariousBathroomTimeScenarios(
            int averageBathroomTime,
            int numberOfVisitsPerDay,
            boolean expectedResult
        ) {
            // given
            King king = new King(
                "TestKing",
                averageBathroomTime,
                numberOfVisitsPerDay,
                BigDecimal.valueOf(50000.0),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
            );

            // when
            boolean isValid = validator.isValid(king, null);

            // then
            assertThat(isValid).isEqualTo(expectedResult);
        }
    }
}
