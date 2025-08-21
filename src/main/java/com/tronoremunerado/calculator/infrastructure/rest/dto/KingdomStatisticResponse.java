package com.tronoremunerado.calculator.infrastructure.rest.dto;

import java.math.BigDecimal;

public record KingdomStatisticResponse(
        int totalKings,
        int totalYearlyMinutesSpent,
        BigDecimal totalYearlyEarnings,
        int maxDailyMinutesSpent) {

}
