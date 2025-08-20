package com.tronoremunerado.calculator.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KingEntity {
    String username;
    int dailyMinutesSpent;
    int monthlyMinutesSpent;
    int yearlyMinutesSpent;
    BigDecimal dailyEarnings;
    BigDecimal monthlyEarnings;
    BigDecimal yearlyEarnings;
    double dailyPercentageOfShift;
}
