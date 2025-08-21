package com.tronoremunerado.calculator.infrastructure.config;

import com.tronoremunerado.calculator.application.ports.output.KingRepositoryPort;
import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.RankingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KingDBConnection implements KingRepositoryPort {

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

    private static final String SELECT_STATISTIC_SQL = String.format("""
            SELECT count(%s), sum(%s), sum(%s), MAX(%s)
            FROM %s
            """,
            COL_ID, COL_YEARLY_MINUTES, COL_YEARLY_EARNINGS, COL_DAILY_MINUTES, TABLE_NAME
    );

    private static final String SELECT_RANKING_SQL = String.format("""
            SELECT %s, %s, %s
            FROM %s
            """,
            COL_USERNAME, COL_DAILY_MINUTES, COL_DAILY_EARNINGS, TABLE_NAME
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveKing(KingEntity king) {
        Assert.notNull(king, "KingEntity must not be null");
        Assert.hasText(king.getUsername(), "Username must not be empty");

        log.info("Saving king data for username: {}", king.getUsername());

        MapSqlParameterSource params = new MapSqlParameterSource()
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

    @Override
    public KingdomEntity getKingdomStatistics() {
        try {
            return jdbcTemplate.queryForObject(SELECT_STATISTIC_SQL, new MapSqlParameterSource(), (rs, rowNum) -> {
                KingdomEntity stats = new KingdomEntity(rs.getInt(1), rs.getInt(2), rs.getBigDecimal(3), rs.getInt(4));
                stats.setTotalKings(rs.getInt(1));
                stats.setTotalYearlyMinutesSpent(rs.getInt(2));
                stats.setTotalYearlyEarnings(rs.getBigDecimal(3));
                stats.setMaxDailyMinutesSpent(rs.getInt(4));
                return stats;
            });
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to retrieve kingdom statistics", e);
        }
    }

    @Override
    public List<RankingEntity> getRanking(RankingType type) {
        String query = SELECT_RANKING_SQL + type.getQueryOrder();
        try {
            return jdbcTemplate.query(query, new MapSqlParameterSource(), (rs, rowNum) -> {
                RankingEntity ranking = new RankingEntity();
                ranking.setUsername(rs.getString(COL_USERNAME));
                ranking.setDailyMinutesSpent(rs.getInt(COL_DAILY_MINUTES));
                ranking.setDailyEarnings(rs.getBigDecimal(COL_DAILY_EARNINGS));
                return ranking;
            });
        } catch (DataAccessException e) {
            log.error("Failed to retrieve ranking for type: {}", type, e);
            throw new RuntimeException("Failed to retrieve ranking", e);
        }
    }
}

