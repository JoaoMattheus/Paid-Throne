package com.tronoremunerado.calculator.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public enum WorkSchedule {

    SIX_ON_ONE(6, 26, 312, 440),
    FIVE_ON_TWO(5, 20, 240, 480),
    FOUR_ON_THREE(4, 16, 192, 480),
    TWELVE_ON_THIRTY_SIX(3, 12, 144, 720);

    private final int daysPerWeek;
    private final int daysPerMonth;
    private final int daysPerYear;
    private final int minutesPerShift;

    WorkSchedule(int daysPerWeek, int daysPerMonth, int daysPerYear, int minutesPerShift) {
        this.daysPerWeek = daysPerWeek;
        this.daysPerMonth = daysPerMonth;
        this.daysPerYear = daysPerYear;
        this.minutesPerShift = minutesPerShift;
    }

    public int getMinutesWorked(ShiftTime shiftTime) {
        return switch (shiftTime) {
            case DAILY -> minutesPerShift;
            case MONTHLY -> daysPerMonth * minutesPerShift;
            case YEARLY -> daysPerYear * minutesPerShift;
        };
    }
}