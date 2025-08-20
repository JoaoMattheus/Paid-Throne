package com.tronoremunerado.calculator.infrastructure.config;

import com.tronoremunerado.calculator.application.ports.output.KingDBConnectionUseInCase;
import com.tronoremunerado.calculator.domain.KingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KingDBConnection implements KingDBConnectionUseInCase {

    private static final String TABLE_NAME = "king";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_DAILY_MINUTES = "daily_minutes_spent";
    private static final String COL_MONTHLY_MINUTES = "monthly_minutes_spent";
    private static final String COL_YEARLY_MINUTES = "yearly_minutes_spent";
    private static final String COL_DAILY_EARNINGS = "daily_earnings";
    private static final String COL_MONTHLY_EARNINGS = "monthly_earnings";
    private static final String COL_YEARLY_EARNINGS = "yearly_earnings";
    private static final String COL_DAILY_PERCENTAGE = "daily_percentage_of_shift";

    private static final String INSERT_SQL = String.format("""
            INSERT INTO %s (
                %s, %s, %s, %s, %s, %s, %s, %s, %s
            ) VALUES (
                :id, :username, :dailyMinutes, :monthlyMinutes, :yearlyMinutes,
                :dailyEarnings, :monthlyEarnings, :yearlyEarnings, :dailyPercentage
            )
            """,
            TABLE_NAME,
            COL_ID, COL_USERNAME, COL_DAILY_MINUTES, COL_MONTHLY_MINUTES, COL_YEARLY_MINUTES,
            COL_DAILY_EARNINGS, COL_MONTHLY_EARNINGS, COL_YEARLY_EARNINGS, COL_DAILY_PERCENTAGE
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveKing(KingEntity king) {
        Assert.notNull(king, "KingEntity must not be null");
        Assert.hasText(king.getUsername(), "Username must not be empty");

        var params = new MapSqlParameterSource()
                .addValue("id", UUID.randomUUID().toString())
                .addValue("username", king.getUsername())
                .addValue("dailyMinutes", king.getDailyMinutesSpent())
                .addValue("monthlyMinutes", king.getMonthlyMinutesSpent())
                .addValue("yearlyMinutes", king.getYearlyMinutesSpent())
                .addValue("dailyEarnings", king.getDailyEarnings())
                .addValue("monthlyEarnings", king.getMonthlyEarnings())
                .addValue("yearlyEarnings", king.getYearlyEarnings())
                .addValue("dailyPercentage", king.getDailyPercentageOfShift());

        try {
            int rowsAffected = jdbcTemplate.update(INSERT_SQL, params);
            
            if (rowsAffected != 1) {
                throw new IllegalStateException("Failed to save king. Expected 1 row to be inserted, but " + rowsAffected + " rows were affected.");
            }
            
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save king data", e);
        }
    }
}

