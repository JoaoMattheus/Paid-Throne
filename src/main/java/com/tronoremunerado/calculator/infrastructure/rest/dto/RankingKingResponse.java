package com.tronoremunerado.calculator.infrastructure.rest.dto;

import java.math.BigDecimal;

public record RankingKingResponse(
        String username,
        int dailyMinutesSpent,
        BigDecimal dailyEarnings) {
}
