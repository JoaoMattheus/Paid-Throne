package com.tronoremunerado.calculator.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KingDBConnectionTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Captor
    private ArgumentCaptor<MapSqlParameterSource> paramsCaptor;

    private KingDBConnection kingDBConnection;
    private KingEntity kingEntity;

    @BeforeEach
    void setUp() {
        kingDBConnection = new KingDBConnection(jdbcTemplate);
        kingEntity = createValidKingEntity();
    }

    @Test
    @DisplayName("Should save king successfully")
    void shouldSaveKingSuccessfully() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);

        // Act
        kingDBConnection.saveKing(kingEntity);

        // Assert
        verify(jdbcTemplate).update(anyString(), paramsCaptor.capture());
        MapSqlParameterSource params = paramsCaptor.getValue();
        
        assertNotNull(params.getValue("id"));
        assertEquals(kingEntity.getUsername(), params.getValue("username"));
        assertEquals(kingEntity.getDailyMinutesSpent(), params.getValue("dailyMinutes"));
        assertEquals(kingEntity.getMonthlyMinutesSpent(), params.getValue("monthlyMinutes"));
        assertEquals(kingEntity.getYearlyMinutesSpent(), params.getValue("yearlyMinutes"));
        assertEquals(kingEntity.getDailyEarnings(), params.getValue("dailyEarnings"));
        assertEquals(kingEntity.getMonthlyEarnings(), params.getValue("monthlyEarnings"));
        assertEquals(kingEntity.getYearlyEarnings(), params.getValue("yearlyEarnings"));
        assertEquals(kingEntity.getDailyPercentageOfShift(), params.getValue("dailyPercentage"));
    }

    @Test
    @DisplayName("Should throw exception when king is null")
    void shouldThrowExceptionWhenKingIsNull() {
        assertThrows(IllegalArgumentException.class, () -> kingDBConnection.saveKing(null));
    }

    @Test
    @DisplayName("Should throw exception when username is empty")
    void shouldThrowExceptionWhenUsernameIsEmpty() {
        kingEntity.setUsername("");
        assertThrows(IllegalArgumentException.class, () -> kingDBConnection.saveKing(kingEntity));
    }

    @Test
    @DisplayName("Should throw exception when database operation fails")
    void shouldThrowExceptionWhenDatabaseOperationFails() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
            .thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        assertThrows(RuntimeException.class, () -> kingDBConnection.saveKing(kingEntity));
    }

    @Test
    @DisplayName("Should throw exception when no rows are affected")
    void shouldThrowExceptionWhenNoRowsAffected() {
        // Arrange
        when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> kingDBConnection.saveKing(kingEntity));
    }

    private KingEntity createValidKingEntity() {
        KingEntity king = new KingEntity();
        king.setUsername("TestKing");
        king.setDailyMinutesSpent(30);
        king.setMonthlyMinutesSpent(600);
        king.setYearlyMinutesSpent(7200);
        king.setDailyEarnings(BigDecimal.valueOf(50));
        king.setMonthlyEarnings(BigDecimal.valueOf(1000));
        king.setYearlyEarnings(BigDecimal.valueOf(12000));
        king.setDailyPercentageOfShift(6.25);
        return king;
    }
}
