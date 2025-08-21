package com.tronoremunerado.calculator.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KingdomEntity {
    private int totalKings;
    private int totalYearlyMinutesSpent;
    private BigDecimal totalYearlyEarnings;
    private int maxDailyMinutesSpent;
}
