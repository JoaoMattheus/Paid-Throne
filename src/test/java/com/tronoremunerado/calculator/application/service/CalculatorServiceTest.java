package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.application.mapper.CalculationResponseMapper;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @Mock
    private CalculationResponseMapper mapper;

    @InjectMocks
    private CalculatorService calculatorService;

    private King king;
    private KingCalculateResponse expectedResponse;

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

        expectedResponse = new KingCalculateResponse(
                "King123",
                30,
                600,
                7200,
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(600),
                BigDecimal.valueOf(7200),
                BigDecimal.valueOf(1000)
        );
    }

    @Test
    @DisplayName("Should calculate salary correctly")
    void shouldCalculateSalaryCorrectly() {
        // Arrange
        when(mapper.toResponse(any(King.class))).thenReturn(expectedResponse);

        // Act
        KingCalculateResponse actualResponse = calculatorService.calculateSalary(king);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.username(), actualResponse.username());
        assertEquals(expectedResponse.dailyMinutesSpent(), actualResponse.dailyMinutesSpent());
        assertEquals(expectedResponse.monthlyMinutesSpent(), actualResponse.monthlyMinutesSpent());
        assertEquals(expectedResponse.yearlyMinutesSpent(), actualResponse.yearlyMinutesSpent());
        assertEquals(expectedResponse.dailyEarnings(), actualResponse.dailyEarnings());
        assertEquals(expectedResponse.monthlyEarnings(), actualResponse.monthlyEarnings());
        assertEquals(expectedResponse.yearlyEarnings(), actualResponse.yearlyEarnings());
        assertEquals(expectedResponse.dailyPercentageOfShift(), actualResponse.dailyPercentageOfShift());

        // Verify mapper interaction
        verify(mapper).toResponse(king);
    }

    @Test
    @DisplayName("Should delegate mapping to CalculationResponseMapper")
    void shouldDelegateMappingToMapper() {
        // Act
        calculatorService.calculateSalary(king);

        // Assert
        verify(mapper).toResponse(king);
    }
}
