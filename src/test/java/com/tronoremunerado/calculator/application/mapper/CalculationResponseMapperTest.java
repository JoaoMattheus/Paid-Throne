package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.application.service.BathroomCalculator;
import com.tronoremunerado.calculator.application.service.SalaryCalculator;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.ShiftTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculationResponseMapperTest {

    private SalaryCalculator salaryCalculator;
    private BathroomCalculator bathroomCalculator;
    private CalculationResponseMapper mapper;

    @BeforeEach
    void setUp() {
        salaryCalculator = mock(SalaryCalculator.class);
        bathroomCalculator = mock(BathroomCalculator.class);
        mapper = new CalculationResponseMapper(salaryCalculator, bathroomCalculator);
    }

    @Test
    void toResponse_shouldHandleZeroValues() {
        // Arrange
        King king = mock(King.class);
        when(king.username()).thenReturn("john");
        when(king.salary()).thenReturn(BigDecimal.ZERO);
        when(king.workSchedule()).thenReturn(WorkSchedule.FIVE_ON_TWO);
        when(king.salaryType()).thenReturn(SalaryType.HOURLY);

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
        assertEquals(0.0, response.dailyPercentageOfShift(), 0.001);
    }

    @Test
    void toResponse_shouldCallSalaryCalculatorWithCorrectArguments() {
        // Arrange
        King king = mock(King.class);
        when(king.username()).thenReturn("leo");
        when(king.salary()).thenReturn(BigDecimal.valueOf(2000));
        when(king.workSchedule()).thenReturn(WorkSchedule.FIVE_ON_TWO);
        when(king.salaryType()).thenReturn(SalaryType.HOURLY);

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