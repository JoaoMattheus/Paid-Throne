package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.application.service.SalaryCalculator;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.ShiftTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculationResponseMapperTest {

    private SalaryCalculator salaryCalculator;
    private CalculationResponseMapper mapper;

    @BeforeEach
    void setUp() {
        salaryCalculator = mock(SalaryCalculator.class);
        mapper = new CalculationResponseMapper(salaryCalculator);
    }

    @Test
    void toResponse_shouldHandleZeroValues() {
        // Arrange
        King king = mock(King.class);
        when(king.username()).thenReturn("john");
        when(king.salary()).thenReturn(BigDecimal.ZERO);

        when(salaryCalculator.calculateMinutesSpent(king, ShiftTime.DAILY)).thenReturn(0);
        when(salaryCalculator.calculateMinutesSpent(king, ShiftTime.MONTHLY)).thenReturn(0);
        when(salaryCalculator.calculateMinutesSpent(king, ShiftTime.YEARLY)).thenReturn(0);

        when(salaryCalculator.calculateEarningsPerMinute(king, ShiftTime.DAILY)).thenReturn(BigDecimal.ZERO);
        when(salaryCalculator.calculateEarningsPerMinute(king, ShiftTime.MONTHLY)).thenReturn(BigDecimal.ZERO);
        when(salaryCalculator.calculateEarningsPerMinute(king, ShiftTime.YEARLY)).thenReturn(BigDecimal.ZERO);

        // Act
        KingCalculateResponse response = mapper.toResponse(king);

        // Assert
        assertEquals("john", response.username());
        assertEquals(0, response.dailyMinutesSpent());
        assertEquals(0, response.monthlyMinutesSpent()); 
        assertEquals(0, response.yearlyMinutesSpent());
        assertEquals(BigDecimal.ZERO, response.dailyEarnings());
        assertEquals(BigDecimal.ZERO, response.monthlyEarnings());
        assertEquals(BigDecimal.ZERO, response.yearlyEarnings());
        assertEquals(BigDecimal.ZERO, response.dailyPercentageOfShift());
    }

    @Test
    void toResponse_shouldCallSalaryCalculatorWithCorrectArguments() {
        // Arrange
        King king = mock(King.class);
        when(king.username()).thenReturn("leo");
        when(king.salary()).thenReturn(BigDecimal.valueOf(2000));

        // Act
        mapper.toResponse(king);

        // Assert
        verify(salaryCalculator).calculateMinutesSpent(king, ShiftTime.DAILY);
        verify(salaryCalculator).calculateMinutesSpent(king, ShiftTime.MONTHLY);
        verify(salaryCalculator).calculateMinutesSpent(king, ShiftTime.YEARLY);
        verify(salaryCalculator).calculateEarningsPerMinute(king, ShiftTime.DAILY);
        verify(salaryCalculator).calculateEarningsPerMinute(king, ShiftTime.MONTHLY);
        verify(salaryCalculator).calculateEarningsPerMinute(king, ShiftTime.YEARLY);
    }
}