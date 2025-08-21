package com.tronoremunerado.calculator.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingEntity {
    private String username;
    private int dailyMinutesSpent;
    private int monthlyMinutesSpent;
    private BigDecimal dailyEarnings;
}
