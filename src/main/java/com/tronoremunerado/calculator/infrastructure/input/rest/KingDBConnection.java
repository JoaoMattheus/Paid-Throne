package com.tronoremunerado.calculator.infrastructure.input.rest;

import com.tronoremunerado.calculator.domain.KingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KingDBConnection implements KingDBConnectionUseInCase {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveKing(KingEntity king) {
        String sql = "INSERT INTO king (" +
                "id, username, daily_minutes_spent, monthly_minutes_spent, yearly_minutes_spent, " +
                "daily_earnings, monthly_earnings, yearly_earnings, daily_percentage_of_shift" +
                ") VALUES (" +
                ":id, :username, :dailyMinutes, :monthlyMinutes, :yearlyMinutes, " +
                ":dailyEarnings, :monthlyEarnings, :yearlyEarnings, :dailyPercentage" +
                ")";

        jdbcTemplate.update(sql, Map.of(
                "id", UUID.randomUUID().toString(),
                "username", king.getUsername(),
                "dailyMinutes", king.getDailyMinutesSpent(),
                "monthlyMinutes", king.getMonthlyMinutesSpent(),
                "yearlyMinutes", king.getYearlyMinutesSpent(),
                "dailyEarnings", king.getDailyEarnings(),
                "monthlyEarnings", king.getMonthlyEarnings(),
                "yearlyEarnings", king.getYearlyEarnings(),
                "dailyPercentage", king.getDailyPercentageOfShift()
        ));
    }
}

