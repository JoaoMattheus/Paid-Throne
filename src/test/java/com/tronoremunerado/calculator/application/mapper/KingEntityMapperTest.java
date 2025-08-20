package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.KingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class KingEntityMapperTest {

    private KingEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new KingEntityMapper();
    }

    @Test
    @DisplayName("Should correctly map KingCalculateResponse to KingEntity")
    void shouldMapKingCalculateResponseToKingEntity() {
        // given
        KingCalculateResponse response = new KingCalculateResponse(
            "KingArthur",
            30,                     // daily minutes
            600,                    // monthly minutes (20 days * 30)
            7200,                   // yearly minutes (12 months * 600)
            BigDecimal.valueOf(25.0), // daily earnings
            BigDecimal.valueOf(500.0), // monthly earnings
            BigDecimal.valueOf(6000.0), // yearly earnings
            6.25                    // daily percentage (30min in 8h = 6.25%)
        );

        // when
        KingEntity entity = mapper.toEntity(response);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getUsername()).isEqualTo("KingArthur");
        assertThat(entity.getDailyMinutesSpent()).isEqualTo(30);
        assertThat(entity.getMonthlyMinutesSpent()).isEqualTo(600);
        assertThat(entity.getYearlyMinutesSpent()).isEqualTo(7200);
        assertThat(entity.getDailyEarnings()).isEqualTo(BigDecimal.valueOf(25.0));
        assertThat(entity.getMonthlyEarnings()).isEqualTo(BigDecimal.valueOf(500.0));
        assertThat(entity.getYearlyEarnings()).isEqualTo(BigDecimal.valueOf(6000.0));
        assertThat(entity.getDailyPercentageOfShift()).isEqualTo(6.25);
    }

    @Test
    @DisplayName("Should handle zero values correctly")
    void shouldHandleZeroValues() {
        // given
        KingCalculateResponse response = new KingCalculateResponse(
            "KingJohn",
            0,                      // no bathroom time
            0,                      
            0,                      
            BigDecimal.ZERO,        // no earnings
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0.0                     // 0% of shift
        );

        // when
        KingEntity entity = mapper.toEntity(response);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getUsername()).isEqualTo("KingJohn");
        assertThat(entity.getDailyMinutesSpent()).isZero();
        assertThat(entity.getMonthlyMinutesSpent()).isZero();
        assertThat(entity.getYearlyMinutesSpent()).isZero();
        assertThat(entity.getDailyEarnings()).isEqualTo(BigDecimal.ZERO);
        assertThat(entity.getMonthlyEarnings()).isEqualTo(BigDecimal.ZERO);
        assertThat(entity.getYearlyEarnings()).isEqualTo(BigDecimal.ZERO);
        assertThat(entity.getDailyPercentageOfShift()).isZero();
    }

    @Test
    @DisplayName("Should handle high precision decimal values")
    void shouldHandleHighPrecisionValues() {
        // given
        KingCalculateResponse response = new KingCalculateResponse(
            "KingRichard",
            45,                     // 45 minutes
            900,                    // 900 minutes monthly
            10800,                  // 10800 minutes yearly
            BigDecimal.valueOf(33.33333), // daily earnings with decimals
            BigDecimal.valueOf(666.66666), // monthly earnings with decimals
            BigDecimal.valueOf(8000.00000), // yearly earnings with decimals
            9.375                   // precise percentage (45min in 8h = 9.375%)
        );

        // when
        KingEntity entity = mapper.toEntity(response);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getUsername()).isEqualTo("KingRichard");
        assertThat(entity.getDailyMinutesSpent()).isEqualTo(45);
        assertThat(entity.getMonthlyMinutesSpent()).isEqualTo(900);
        assertThat(entity.getYearlyMinutesSpent()).isEqualTo(10800);
        assertThat(entity.getDailyEarnings()).isEqualTo(BigDecimal.valueOf(33.33333));
        assertThat(entity.getMonthlyEarnings()).isEqualTo(BigDecimal.valueOf(666.66666));
        assertThat(entity.getYearlyEarnings()).isEqualTo(BigDecimal.valueOf(8000.00000));
        assertThat(entity.getDailyPercentageOfShift()).isEqualTo(9.375);
    }
}
