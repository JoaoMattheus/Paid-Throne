package com.tronoremunerado.calculator.domain;

import java.math.BigDecimal;

public record KingCalculateResponse(
        String username,
        int dailyMinutesSpent,
        int monthlyMinutesSpent,
        int yearlyMinutesSpent,
        BigDecimal dailyEarnings,
        BigDecimal monthlyEarnings,
        BigDecimal yearlyEarnings,
        double dailyPercentageOfShift
) {}

