package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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
}
