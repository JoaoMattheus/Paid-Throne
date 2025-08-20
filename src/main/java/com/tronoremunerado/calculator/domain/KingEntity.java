package com.tronoremunerado.calculator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
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
