package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.ShiftTime;
import com.tronoremunerado.calculator.domain.SalaryType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SalaryCalculator {

    public int calculateMinutesSpent(King king, ShiftTime shiftTime) {
        int minutesOnBathroomPerDay = king.averageBathroomTime() * king.numberOfVisitsPerDay();
        return switch (shiftTime) {
            case DAILY -> minutesOnBathroomPerDay;
            case MONTHLY -> minutesOnBathroomPerDay * king.workSchedule().getDaysPerMonth();
            case YEARLY -> minutesOnBathroomPerDay * king.workSchedule().getDaysPerYear();
        };
    }

    public BigDecimal calculateEarningsPerMinute(King king, ShiftTime shiftTime) {
        BigDecimal salaryPerMinute = switch (king.salaryType()) {
            case HOURLY -> king.salary().divide(BigDecimal.valueOf(60), 2, RoundingMode.DOWN);
            case DAILY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.DAILY)), 2, RoundingMode.DOWN);
            case MONTHLY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.MONTHLY)), 2, RoundingMode.DOWN);
            case YEARLY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.YEARLY)), 2, RoundingMode.DOWN);
        };

        return salaryPerMinute.multiply(BigDecimal.valueOf(calculateMinutesSpent(king, shiftTime)));
    }
}
