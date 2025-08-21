package com.tronoremunerado.calculator.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RankingTypeTest {

    @Test
    void shouldReturnCorrectQueryOrderForHigherEarnings() {
        // Given
        RankingType type = RankingType.HIGHER_EARNINGS;
        
        // When
        String queryOrder = type.getQueryOrder();
        
        // Then
        assertThat(queryOrder).isEqualTo(" order by daily_earnings desc, username asc limit 5;");
    }

    @Test
    void shouldReturnCorrectQueryOrderForHigherMinutes() {
        // Given
        RankingType type = RankingType.HIGHER_MINUTES;
        
        // When
        String queryOrder = type.getQueryOrder();
        
        // Then
        assertThat(queryOrder).isEqualTo(" order by daily_minutes_spent desc, username asc limit 5;");
    }

    @Test
    void shouldReturnCorrectQueryOrderForLowerMinutes() {
        // Given
        RankingType type = RankingType.LOWER_MINUTES;
        
        // When
        String queryOrder = type.getQueryOrder();
        
        // Then
        assertThat(queryOrder).isEqualTo(" order by daily_minutes_spent asc, username asc limit 5;");
    }

    @Test
    void shouldHaveAllExpectedValues() {
        // Given & When
        RankingType[] values = RankingType.values();
        
        // Then
        assertThat(values).hasSize(3);
        assertThat(values).containsExactly(
            RankingType.HIGHER_EARNINGS,
            RankingType.HIGHER_MINUTES,
            RankingType.LOWER_MINUTES
        );
    }
}
