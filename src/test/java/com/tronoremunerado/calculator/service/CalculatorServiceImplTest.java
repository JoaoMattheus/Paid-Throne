package com.tronoremunerado.calculator.service;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceImplTest {

    @InjectMocks
    private CalculatorServiceImpl service;

    @Test
    void testCalculate_returnsNonNullResponse() {
        King king = new King(
                "joao",
                15,
                3,
                BigDecimal.valueOf(2500),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        KingCalculateResponse response = service.calculate(king);

        assertNotNull(response, "Response should not be null");
    }

    @Test
    void testCalculateDailyMinutes() {
        King king = new King(
                "maria",
                20,
                2,
                BigDecimal.valueOf(3000),
                SalaryType.MONTHLY,
                WorkSchedule.SIX_ON_ONE
        );

        KingCalculateResponse response = service.calculate(king);

        int expectedDailyMinutes = 20 * 2; // tempo médio * número de idas
        assertEquals(expectedDailyMinutes, response.dailyMinutesSpent());
    }

    @Test
    void testCalculateMonthlyMinutes() {
        King king = new King(
                "pedro",
                10,
                3,
                BigDecimal.valueOf(4000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        KingCalculateResponse response = service.calculate(king);

        int expectedMonthlyMinutes = 10 * 3 * WorkSchedule.FIVE_ON_TWO.getDaysPerMonth();
        assertEquals(expectedMonthlyMinutes, response.monthlyMinutesSpent());
    }

    @Test
    void testCalculateDailyPercentageOfShift() {
        King king = new King(
                "ana",
                15,
                4,
                BigDecimal.valueOf(3500),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        KingCalculateResponse response = service.calculate(king);

        double expectedPercentage = ((15 * 4) / (double) WorkSchedule.FIVE_ON_TWO.getMinutesPerShift()) * 100;
        assertEquals(expectedPercentage, response.dailyPercentageOfShift(), 0.01);
    }
}
