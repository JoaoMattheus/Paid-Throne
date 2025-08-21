package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.RankingEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.RankingKingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KingdomStatisticResponseMapperTest {

    @InjectMocks
    private KingdomStatisticResponseMapper mapper;

    private KingdomEntity kingdomEntity;

    @BeforeEach
    void setUp() {
        kingdomEntity = new KingdomEntity(
                5,
                36000,
                BigDecimal.valueOf(60000.50),
                120
        );
    }

    @Test
    @DisplayName("Should map KingdomEntity to KingdomStatisticResponse successfully")
    void shouldMapKingdomEntityToResponseSuccessfully() {
        // Act
        KingdomStatisticResponse response = mapper.toKingdomStatisticResponse(kingdomEntity);

        // Assert
        assertNotNull(response);
        assertEquals(kingdomEntity.getTotalKings(), response.totalKings());
        assertEquals(kingdomEntity.getTotalYearlyMinutesSpent(), response.totalYearlyMinutesSpent());
        assertEquals(kingdomEntity.getTotalYearlyEarnings(), response.totalYearlyEarnings());
        assertEquals(kingdomEntity.getMaxDailyMinutesSpent(), response.maxDailyMinutesSpent());
    }

    @Test
    @DisplayName("Should handle zero values correctly")
    void shouldHandleZeroValuesCorrectly() {
        // Arrange
        KingdomEntity emptyKingdom = new KingdomEntity(
                0,
                0,
                BigDecimal.ZERO,
                0
        );

        // Act
        KingdomStatisticResponse response = mapper.toKingdomStatisticResponse(emptyKingdom);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.totalKings());
        assertEquals(0, response.totalYearlyMinutesSpent());
        assertEquals(BigDecimal.ZERO, response.totalYearlyEarnings());
        assertEquals(0, response.maxDailyMinutesSpent());
    }

    @Test
    @DisplayName("Should handle large values correctly")
    void shouldHandleLargeValuesCorrectly() {
        // Arrange
        KingdomEntity largeKingdom = new KingdomEntity(
                1000,
                525600000, // Many minutes
                BigDecimal.valueOf(999999999.99),
                1440 // Full day in minutes
        );

        // Act
        KingdomStatisticResponse response = mapper.toKingdomStatisticResponse(largeKingdom);

        // Assert
        assertNotNull(response);
        assertEquals(1000, response.totalKings());
        assertEquals(525600000, response.totalYearlyMinutesSpent());
        assertEquals(BigDecimal.valueOf(999999999.99), response.totalYearlyEarnings());
        assertEquals(1440, response.maxDailyMinutesSpent());
    }

    @Test
    @DisplayName("Should handle null KingdomEntity gracefully")
    void shouldHandleNullKingdomEntityGracefully() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            mapper.toKingdomStatisticResponse(null);
        });
    }

    @Test
    @DisplayName("Should preserve decimal precision in earnings")
    void shouldPreserveDecimalPrecisionInEarnings() {
        // Arrange
        BigDecimal preciseEarnings = BigDecimal.valueOf(12345.678);
        KingdomEntity preciseKingdom = new KingdomEntity(
                3,
                1800,
                preciseEarnings,
                60
        );

        // Act
        KingdomStatisticResponse response = mapper.toKingdomStatisticResponse(preciseKingdom);

        // Assert
        assertNotNull(response);
        assertEquals(preciseEarnings, response.totalYearlyEarnings());
        assertEquals(0, preciseEarnings.compareTo(response.totalYearlyEarnings()));
    }

    // ========== RANKING MAPPING TESTS ==========

    @Test
    @DisplayName("Should map empty RankingEntity list to empty RankingKingResponse list")
    void shouldMapEmptyRankingEntityListToEmptyRankingKingResponseList() {
        // Arrange
        List<RankingEntity> entities = Collections.emptyList();

        // Act
        List<RankingKingResponse> responses = mapper.toRankingKingResponseList(entities);

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should map RankingEntity list to RankingKingResponse list successfully")
    void shouldMapRankingEntityListToRankingKingResponseListSuccessfully() {
        // Arrange
        List<RankingEntity> entities = Arrays.asList(
            createRankingEntity("king1", 120, BigDecimal.valueOf(50.0)),
            createRankingEntity("king2", 100, BigDecimal.valueOf(45.0)),
            createRankingEntity("king3", 80, BigDecimal.valueOf(40.0))
        );

        // Act
        List<RankingKingResponse> responses = mapper.toRankingKingResponseList(entities);

        // Assert
        assertNotNull(responses);
        assertEquals(3, responses.size());

        // Verify first entity mapping
        RankingKingResponse first = responses.get(0);
        assertEquals("king1", first.username());
        assertEquals(120, first.dailyMinutesSpent());
        assertEquals(BigDecimal.valueOf(50.0), first.dailyEarnings());

        // Verify second entity mapping
        RankingKingResponse second = responses.get(1);
        assertEquals("king2", second.username());
        assertEquals(100, second.dailyMinutesSpent());
        assertEquals(BigDecimal.valueOf(45.0), second.dailyEarnings());

        // Verify third entity mapping
        RankingKingResponse third = responses.get(2);
        assertEquals("king3", third.username());
        assertEquals(80, third.dailyMinutesSpent());
        assertEquals(BigDecimal.valueOf(40.0), third.dailyEarnings());
    }

    @Test
    @DisplayName("Should map single RankingEntity to RankingKingResponse list")
    void shouldMapSingleRankingEntityToRankingKingResponseList() {
        // Arrange
        List<RankingEntity> entities = Arrays.asList(
            createRankingEntity("singleKing", 200, BigDecimal.valueOf(75.5))
        );

        // Act
        List<RankingKingResponse> responses = mapper.toRankingKingResponseList(entities);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());

        RankingKingResponse response = responses.get(0);
        assertEquals("singleKing", response.username());
        assertEquals(200, response.dailyMinutesSpent());
        assertEquals(BigDecimal.valueOf(75.5), response.dailyEarnings());
    }

    @Test
    @DisplayName("Should map RankingEntity with special characters in username")
    void shouldMapRankingEntityWithSpecialCharactersInUsername() {
        // Arrange
        List<RankingEntity> entities = Arrays.asList(
            createRankingEntity("king@domain.com", 90, BigDecimal.valueOf(30.25)),
            createRankingEntity("king-with-dashes", 85, BigDecimal.valueOf(28.75)),
            createRankingEntity("king_with_underscores", 95, BigDecimal.valueOf(32.50))
        );

        // Act
        List<RankingKingResponse> responses = mapper.toRankingKingResponseList(entities);

        // Assert
        assertNotNull(responses);
        assertEquals(3, responses.size());
        assertEquals("king@domain.com", responses.get(0).username());
        assertEquals("king-with-dashes", responses.get(1).username());
        assertEquals("king_with_underscores", responses.get(2).username());
    }

    @Test
    @DisplayName("Should map RankingEntity with zero values")
    void shouldMapRankingEntityWithZeroValues() {
        // Arrange
        List<RankingEntity> entities = Arrays.asList(
            createRankingEntity("zeroKing", 0, BigDecimal.ZERO)
        );

        // Act
        List<RankingKingResponse> responses = mapper.toRankingKingResponseList(entities);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());

        RankingKingResponse response = responses.get(0);
        assertEquals("zeroKing", response.username());
        assertEquals(0, response.dailyMinutesSpent());
        assertEquals(BigDecimal.ZERO, response.dailyEarnings());
    }

    @Test
    @DisplayName("Should map RankingEntity with large values")
    void shouldMapRankingEntityWithLargeValues() {
        // Arrange
        List<RankingEntity> entities = Arrays.asList(
            createRankingEntity("richKing", Integer.MAX_VALUE, BigDecimal.valueOf(Double.MAX_VALUE))
        );

        // Act
        List<RankingKingResponse> responses = mapper.toRankingKingResponseList(entities);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());

        RankingKingResponse response = responses.get(0);
        assertEquals("richKing", response.username());
        assertEquals(Integer.MAX_VALUE, response.dailyMinutesSpent());
        assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), response.dailyEarnings());
    }

    @Test
    @DisplayName("Should preserve order in RankingEntity list mapping")
    void shouldPreserveOrderInRankingEntityListMapping() {
        // Arrange - Order should be preserved as returned from database
        List<RankingEntity> entities = Arrays.asList(
            createRankingEntity("third", 80, BigDecimal.valueOf(30.0)),
            createRankingEntity("first", 120, BigDecimal.valueOf(50.0)),
            createRankingEntity("second", 100, BigDecimal.valueOf(40.0))
        );

        // Act
        List<RankingKingResponse> responses = mapper.toRankingKingResponseList(entities);

        // Assert
        assertNotNull(responses);
        assertEquals(3, responses.size());
        assertEquals("third", responses.get(0).username());
        assertEquals("first", responses.get(1).username());
        assertEquals("second", responses.get(2).username());
    }

    // Helper method
    private RankingEntity createRankingEntity(String username, int dailyMinutes, BigDecimal dailyEarnings) {
        RankingEntity entity = new RankingEntity();
        entity.setUsername(username);
        entity.setDailyMinutesSpent(dailyMinutes);
        entity.setDailyEarnings(dailyEarnings);
        return entity;
    }
}
