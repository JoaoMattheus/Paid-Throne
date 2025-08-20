package com.tronoremunerado.calculator.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.ShiftTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SalaryCalculator {

    public int calculateMinutesSpent(King king, ShiftTime shiftTime) {
        log.info("Calculating minutes spent for user {} in period {}", king.username(), shiftTime);
        
        int minutesOnBathroomPerDay = king.averageBathroomTime() * king.numberOfVisitsPerDay();
        int result = switch (shiftTime) {
            case DAILY -> minutesOnBathroomPerDay;
            case MONTHLY -> minutesOnBathroomPerDay * king.workSchedule().getDaysPerMonth();
            case YEARLY -> minutesOnBathroomPerDay * king.workSchedule().getDaysPerYear();
        };

        log.info("Minutes spent calculated for user {} in period {}: {} minutes", king.username(), shiftTime, result);
        return result;
    }

    public BigDecimal calculateEarningsPerMinute(King king, ShiftTime shiftTime) {
        log.info("Calculating earnings per minute for user {} - salary type: {}, period: {}", 
                 king.username(), king.salaryType(), shiftTime);
        
        BigDecimal salaryPerMinute = switch (king.salaryType()) {
            case HOURLY -> king.salary().divide(BigDecimal.valueOf(60), 2, RoundingMode.DOWN);
            case DAILY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.DAILY)), 2, RoundingMode.DOWN);
            case MONTHLY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.MONTHLY)), 2, RoundingMode.DOWN);
            case YEARLY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.YEARLY)), 2, RoundingMode.DOWN);
        };

        log.info("Salary per minute calculated: {}", salaryPerMinute);

        BigDecimal totalEarnings = salaryPerMinute.multiply(BigDecimal.valueOf(calculateMinutesSpent(king, shiftTime)));

        log.info("Total earnings calculated for {}: {}", shiftTime, totalEarnings);
        return totalEarnings;
    }
}
