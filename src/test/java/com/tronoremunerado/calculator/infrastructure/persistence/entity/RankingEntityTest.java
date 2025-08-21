package com.tronoremunerado.calculator.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RankingEntityTest {

    @Test
    @DisplayName("Should create RankingEntity with no args constructor")
    void shouldCreateRankingEntityWithNoArgsConstructor() {
        // Act
        RankingEntity entity = new RankingEntity();

        // Assert
        assertNotNull(entity);
        assertNull(entity.getUsername());
        assertEquals(0, entity.getDailyMinutesSpent());
        assertEquals(0, entity.getMonthlyMinutesSpent());
        assertNull(entity.getDailyEarnings());
    }

    @Test
    @DisplayName("Should create RankingEntity with all args constructor")
    void shouldCreateRankingEntityWithAllArgsConstructor() {
        // Arrange
        String username = "testKing";
        int dailyMinutes = 120;
        int monthlyMinutes = 2400;
        BigDecimal dailyEarnings = BigDecimal.valueOf(50.0);

        // Act
        RankingEntity entity = new RankingEntity(username, dailyMinutes, monthlyMinutes, dailyEarnings);

        // Assert
        assertNotNull(entity);
        assertEquals(username, entity.getUsername());
        assertEquals(dailyMinutes, entity.getDailyMinutesSpent());
        assertEquals(monthlyMinutes, entity.getMonthlyMinutesSpent());
        assertEquals(dailyEarnings, entity.getDailyEarnings());
    }

    @Test
    @DisplayName("Should set and get username correctly")
    void shouldSetAndGetUsernameCorrectly() {
        // Arrange
        RankingEntity entity = new RankingEntity();
        String username = "king123";

        // Act
        entity.setUsername(username);

        // Assert
        assertEquals(username, entity.getUsername());
    }

    @Test
    @DisplayName("Should set and get daily minutes spent correctly")
    void shouldSetAndGetDailyMinutesSpentCorrectly() {
        // Arrange
        RankingEntity entity = new RankingEntity();
        int dailyMinutes = 90;

        // Act
        entity.setDailyMinutesSpent(dailyMinutes);

        // Assert
        assertEquals(dailyMinutes, entity.getDailyMinutesSpent());
    }

    @Test
    @DisplayName("Should set and get monthly minutes spent correctly")
    void shouldSetAndGetMonthlyMinutesSpentCorrectly() {
        // Arrange
        RankingEntity entity = new RankingEntity();
        int monthlyMinutes = 1800;

        // Act
        entity.setMonthlyMinutesSpent(monthlyMinutes);

        // Assert
        assertEquals(monthlyMinutes, entity.getMonthlyMinutesSpent());
    }

    @Test
    @DisplayName("Should set and get daily earnings correctly")
    void shouldSetAndGetDailyEarningsCorrectly() {
        // Arrange
        RankingEntity entity = new RankingEntity();
        BigDecimal dailyEarnings = BigDecimal.valueOf(35.50);

        // Act
        entity.setDailyEarnings(dailyEarnings);

        // Assert
        assertEquals(dailyEarnings, entity.getDailyEarnings());
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValuesCorrectly() {
        // Arrange
        RankingEntity entity = new RankingEntity();

        // Act
        entity.setUsername(null);
        entity.setDailyEarnings(null);

        // Assert
        assertNull(entity.getUsername());
        assertNull(entity.getDailyEarnings());
    }

    @Test
    @DisplayName("Should handle zero values correctly")
    void shouldHandleZeroValuesCorrectly() {
        // Arrange
        RankingEntity entity = new RankingEntity();

        // Act
        entity.setDailyMinutesSpent(0);
        entity.setMonthlyMinutesSpent(0);
        entity.setDailyEarnings(BigDecimal.ZERO);

        // Assert
        assertEquals(0, entity.getDailyMinutesSpent());
        assertEquals(0, entity.getMonthlyMinutesSpent());
        assertEquals(BigDecimal.ZERO, entity.getDailyEarnings());
    }

    @Test
    @DisplayName("Should handle negative values")
    void shouldHandleNegativeValues() {
        // Arrange
        RankingEntity entity = new RankingEntity();

        // Act
        entity.setDailyMinutesSpent(-10);
        entity.setMonthlyMinutesSpent(-200);
        entity.setDailyEarnings(BigDecimal.valueOf(-5.0));

        // Assert
        assertEquals(-10, entity.getDailyMinutesSpent());
        assertEquals(-200, entity.getMonthlyMinutesSpent());
        assertEquals(BigDecimal.valueOf(-5.0), entity.getDailyEarnings());
    }

    @Test
    @DisplayName("Should handle large values")
    void shouldHandleLargeValues() {
        // Arrange
        RankingEntity entity = new RankingEntity();

        // Act
        entity.setDailyMinutesSpent(Integer.MAX_VALUE);
        entity.setMonthlyMinutesSpent(Integer.MAX_VALUE);
        entity.setDailyEarnings(BigDecimal.valueOf(Double.MAX_VALUE));

        // Assert
        assertEquals(Integer.MAX_VALUE, entity.getDailyMinutesSpent());
        assertEquals(Integer.MAX_VALUE, entity.getMonthlyMinutesSpent());
        assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), entity.getDailyEarnings());
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void shouldHandleSpecialCharactersInUsername() {
        // Arrange
        RankingEntity entity = new RankingEntity();
        String specialUsername = "king@domain.com_123-test";

        // Act
        entity.setUsername(specialUsername);

        // Assert
        assertEquals(specialUsername, entity.getUsername());
    }

    @Test
    @DisplayName("Should preserve BigDecimal precision")
    void shouldPreserveBigDecimalPrecision() {
        // Arrange
        RankingEntity entity = new RankingEntity();
        BigDecimal preciseEarnings = BigDecimal.valueOf(123.456789);

        // Act
        entity.setDailyEarnings(preciseEarnings);

        // Assert
        assertEquals(preciseEarnings, entity.getDailyEarnings());
        assertEquals(0, preciseEarnings.compareTo(entity.getDailyEarnings()));
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        // Arrange
        String username = "testKing";
        int dailyMinutes = 120;
        int monthlyMinutes = 2400;
        BigDecimal dailyEarnings = BigDecimal.valueOf(50.0);

        RankingEntity entity1 = new RankingEntity(username, dailyMinutes, monthlyMinutes, dailyEarnings);
        RankingEntity entity2 = new RankingEntity(username, dailyMinutes, monthlyMinutes, dailyEarnings);
        RankingEntity entity3 = new RankingEntity("differentKing", dailyMinutes, monthlyMinutes, dailyEarnings);

        // Assert
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1, entity3);
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    @DisplayName("Should test toString method")
    void shouldTestToStringMethod() {
        // Arrange
        RankingEntity entity = new RankingEntity(
                "testKing",
                120,
                2400,
                BigDecimal.valueOf(50.0)
        );

        // Act
        String toString = entity.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("testKing"));
        assertTrue(toString.contains("120"));
        assertTrue(toString.contains("2400"));
        assertTrue(toString.contains("50"));
    }
}
