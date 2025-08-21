package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.ShiftTime;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryCalculatorTest {

    private SalaryCalculator calculator;
    private King king;

    @BeforeEach
    void setUp() {
        calculator = new SalaryCalculator();
    }

    @Nested
    @DisplayName("Calculate Minutes Spent Tests")
    class CalculateMinutesSpentTests {

        @BeforeEach
        void setUp() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(1000),
                    SalaryType.HOURLY,
                    WorkSchedule.FIVE_ON_TWO
            );
        }

        @ParameterizedTest
        @EnumSource(ShiftTime.class)
        @DisplayName("Should calculate minutes spent correctly for all shift times")
        void shouldCalculateMinutesSpentCorrectly(ShiftTime shiftTime) {
            int expectedMinutes = switch (shiftTime) {
                case DAILY -> 30; // 10 minutes * 3 visits
                case MONTHLY -> 600; // 30 minutes * 20 days
                case YEARLY -> 7200; // 30 minutes * 240 days
            };

            int actualMinutes = calculator.calculateMinutesSpent(king, shiftTime);

            assertEquals(expectedMinutes, actualMinutes);
        }
    }

    @Nested
    @DisplayName("Calculate Total Earnings in Bathroom Tests")
    class CalculateTotalEarningsInBathroomTests {

        @Test
        @DisplayName("Should calculate hourly salary earnings correctly")
        void shouldCalculateHourlySalaryEarningsCorrectly() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(60), // R$60 per hour = R$1 per minute
                    SalaryType.HOURLY,
                    WorkSchedule.FIVE_ON_TWO
            );

            BigDecimal earnings = calculator.calculateTotalEarningsInBathroom(king, ShiftTime.DAILY);
            
            // For hourly rate of R$60, each minute is R$1
            // With 30 minutes spent (10 min * 3 visits), expected earnings is R$30
            assertEquals(BigDecimal.valueOf(30).setScale(2, RoundingMode.DOWN), earnings);
        }

        @Test
        @DisplayName("Should calculate daily salary earnings correctly")
        void shouldCalculateDailySalaryEarningsCorrectly() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(480), // R$480 per day (for 480 minutes worked)
                    SalaryType.DAILY,
                    WorkSchedule.FIVE_ON_TWO
            );

            BigDecimal earnings = calculator.calculateTotalEarningsInBathroom(king, ShiftTime.DAILY);
            
            // R$480/480 minutes = R$1 per minute
            // With 30 minutes spent, expected earnings is R$30
            assertEquals(BigDecimal.valueOf(30).setScale(2, RoundingMode.DOWN), earnings);
        }

        @Test
        @DisplayName("Should calculate monthly salary earnings correctly")
        void shouldCalculateMonthlySalaryEarningsCorrectly() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(9600), // R$9600 per month (for 9600 minutes worked)
                    SalaryType.MONTHLY,
                    WorkSchedule.FIVE_ON_TWO
            );

            BigDecimal earnings = calculator.calculateTotalEarningsInBathroom(king, ShiftTime.MONTHLY);
            
            // R$9600/9600 minutes = R$1 per minute
            // With 600 minutes spent monthly (30 min * 20 days), expected earnings is R$600
            assertEquals(BigDecimal.valueOf(600).setScale(2, RoundingMode.DOWN), earnings);
        }

        @Test
        @DisplayName("Should calculate yearly salary earnings correctly")
        void shouldCalculateYearlySalaryEarningsCorrectly() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(115200), // R$115200 per year (for 115200 minutes worked)
                    SalaryType.YEARLY,
                    WorkSchedule.FIVE_ON_TWO
            );

            BigDecimal earnings = calculator.calculateTotalEarningsInBathroom(king, ShiftTime.YEARLY);
            
            // R$115200/115200 minutes = R$1 per minute
            // With 7200 minutes spent yearly (30 min * 240 days), expected earnings is R$7200
            assertEquals(BigDecimal.valueOf(7200).setScale(2, RoundingMode.DOWN), earnings);
        }
    }
    
    @Nested
    @DisplayName("Calculate Salary Per Minute Tests")
    class CalculateSalaryPerMinuteTests {

        @Test
        @DisplayName("Should calculate salary per minute for hourly wage correctly")
        void shouldCalculateSalaryPerMinuteForHourlyWageCorrectly() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(120), // R$120 per hour
                    SalaryType.HOURLY,
                    WorkSchedule.FIVE_ON_TWO
            );

            BigDecimal salaryPerMinute = calculator.calculateSalaryPerMinute(king);
            
            // R$120 per hour = R$2 per minute
            assertEquals(BigDecimal.valueOf(2.00).setScale(2, RoundingMode.DOWN), salaryPerMinute);
        }

        @Test
        @DisplayName("Should calculate salary per minute for daily wage correctly")
        void shouldCalculateSalaryPerMinuteForDailyWageCorrectly() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(480), // R$480 per day
                    SalaryType.DAILY,
                    WorkSchedule.FIVE_ON_TWO // 8 hours = 480 minutes
            );

            BigDecimal salaryPerMinute = calculator.calculateSalaryPerMinute(king);
            
            // R$480 per day / 480 minutes = R$1 per minute
            assertEquals(BigDecimal.valueOf(1.00).setScale(2, RoundingMode.DOWN), salaryPerMinute);
        }

        @Test
        @DisplayName("Should calculate salary per minute for monthly wage correctly")
        void shouldCalculateSalaryPerMinuteForMonthlyWageCorrectly() {
            king = new King(
                    "King123",
                    10,
                    3,
                    BigDecimal.valueOf(9600), // R$9,600 per month
                    SalaryType.MONTHLY,
                    WorkSchedule.FIVE_ON_TWO // 20 days * 480 minutes = 9600 minutes per month
            );

            BigDecimal salaryPerMinute = calculator.calculateSalaryPerMinute(king);
            
            // R$9,600 per month / 9600 minutes = R$1 per minute
            assertEquals(BigDecimal.valueOf(1.00).setScale(2, RoundingMode.DOWN), salaryPerMinute);
        }
    }
}
