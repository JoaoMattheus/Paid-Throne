package com.tronoremunerado.calculator.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record King(
        String username,
        int averageBathroomTime,
        int numberOfVisitsPerDay,
        BigDecimal salary,
        SalaryType salaryType,
        WorkSchedule workSchedule
) {
    public int getMinutesSpent(ShiftTime shiftTime) {
        int minutesOnBathroomPerDay = averageBathroomTime * numberOfVisitsPerDay;
        return switch (shiftTime) {
            case DAILY -> minutesOnBathroomPerDay;
            case MONTHLY -> minutesOnBathroomPerDay * workSchedule.getDaysPerMonth();
            case YEARLY -> minutesOnBathroomPerDay * workSchedule.getDaysPerYear();
        };
    }

    public BigDecimal getEarningsPerMinute(ShiftTime shiftTime) {
        BigDecimal salaryPerMinute = switch (salaryType) {
            case HOURLY -> salary.divide(BigDecimal.valueOf(60), 2, RoundingMode.DOWN);
            case DAILY -> salary.divide(BigDecimal.valueOf(workSchedule.getMinutesWorked(ShiftTime.DAILY)), 2, RoundingMode.DOWN); // Assuming 8 hours workday
            case MONTHLY -> salary.divide(BigDecimal.valueOf(workSchedule.getMinutesWorked(ShiftTime.MONTHLY)), 2, RoundingMode.DOWN); // Assuming 30 days month
            case YEARLY -> salary.divide(BigDecimal.valueOf(workSchedule.getMinutesWorked(ShiftTime.YEARLY)), 2, RoundingMode.DOWN); // Assuming 365 days year
        };

        return salaryPerMinute.multiply(BigDecimal.valueOf(getMinutesSpent(shiftTime)));
    }


}
