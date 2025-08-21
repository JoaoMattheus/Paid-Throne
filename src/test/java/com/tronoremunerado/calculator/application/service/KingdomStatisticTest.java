package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.application.mapper.KingdomStatisticResponseMapper;
import com.tronoremunerado.calculator.application.ports.output.KingRepositoryPort;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KingdomStatisticTest {

    @Mock
    private KingRepositoryPort kingRepositoryPort;

    @Mock
    private KingdomStatisticResponseMapper mapper;

    @InjectMocks
    private KingdomStatistic kingdomStatistic;

    private KingdomEntity mockKingdomEntity;
    private KingdomStatisticResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockKingdomEntity = new KingdomEntity(
                10,
                72000,
                BigDecimal.valueOf(120000.75),
                180
        );

        mockResponse = new KingdomStatisticResponse(
                10,
                72000,
                BigDecimal.valueOf(120000.75),
                180
        );
    }

    @Test
    @DisplayName("Should get kingdom statistics successfully")
    void shouldGetKingdomStatisticsSuccessfully() {
        // Arrange
        when(kingRepositoryPort.getKingdomStatistics()).thenReturn(mockKingdomEntity);
        when(mapper.toKingdomStatisticResponse(mockKingdomEntity)).thenReturn(mockResponse);

        // Act
        KingdomStatisticResponse result = kingdomStatistic.getKingdomStatistics();

        // Assert
        assertNotNull(result);
        assertEquals(mockResponse.totalKings(), result.totalKings());
        assertEquals(mockResponse.totalYearlyMinutesSpent(), result.totalYearlyMinutesSpent());
        assertEquals(mockResponse.totalYearlyEarnings(), result.totalYearlyEarnings());
        assertEquals(mockResponse.maxDailyMinutesSpent(), result.maxDailyMinutesSpent());

        verify(kingRepositoryPort, times(1)).getKingdomStatistics();
        verify(mapper, times(1)).toKingdomStatisticResponse(mockKingdomEntity);
    }

    @Test
    @DisplayName("Should handle empty kingdom statistics")
    void shouldHandleEmptyKingdomStatistics() {
        // Arrange
        KingdomEntity emptyEntity = new KingdomEntity(0, 0, BigDecimal.ZERO, 0);
        KingdomStatisticResponse emptyResponse = new KingdomStatisticResponse(0, 0, BigDecimal.ZERO, 0);

        when(kingRepositoryPort.getKingdomStatistics()).thenReturn(emptyEntity);
        when(mapper.toKingdomStatisticResponse(emptyEntity)).thenReturn(emptyResponse);

        // Act
        KingdomStatisticResponse result = kingdomStatistic.getKingdomStatistics();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.totalKings());
        assertEquals(0, result.totalYearlyMinutesSpent());
        assertEquals(BigDecimal.ZERO, result.totalYearlyEarnings());
        assertEquals(0, result.maxDailyMinutesSpent());

        verify(kingRepositoryPort, times(1)).getKingdomStatistics();
        verify(mapper, times(1)).toKingdomStatisticResponse(emptyEntity);
    }

    @Test
    @DisplayName("Should propagate repository exception")
    void shouldPropagateRepositoryException() {
        // Arrange
        RuntimeException repositoryException = new RuntimeException("Database connection failed");
        when(kingRepositoryPort.getKingdomStatistics()).thenThrow(repositoryException);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kingdomStatistic.getKingdomStatistics();
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(kingRepositoryPort, times(1)).getKingdomStatistics();
        verify(mapper, never()).toKingdomStatisticResponse(any());
    }

    @Test
    @DisplayName("Should propagate mapper exception")
    void shouldPropagateMapperException() {
        // Arrange
        when(kingRepositoryPort.getKingdomStatistics()).thenReturn(mockKingdomEntity);
        when(mapper.toKingdomStatisticResponse(mockKingdomEntity))
                .thenThrow(new RuntimeException("Mapping failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kingdomStatistic.getKingdomStatistics();
        });

        assertEquals("Mapping failed", exception.getMessage());
        verify(kingRepositoryPort, times(1)).getKingdomStatistics();
        verify(mapper, times(1)).toKingdomStatisticResponse(mockKingdomEntity);
    }

    @Test
    @DisplayName("Should handle large statistics values correctly")
    void shouldHandleLargeStatisticsValuesCorrectly() {
        // Arrange
        KingdomEntity largeEntity = new KingdomEntity(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                BigDecimal.valueOf(Double.MAX_VALUE),
                Integer.MAX_VALUE
        );
        KingdomStatisticResponse largeResponse = new KingdomStatisticResponse(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                BigDecimal.valueOf(Double.MAX_VALUE),
                Integer.MAX_VALUE
        );

        when(kingRepositoryPort.getKingdomStatistics()).thenReturn(largeEntity);
        when(mapper.toKingdomStatisticResponse(largeEntity)).thenReturn(largeResponse);

        // Act
        KingdomStatisticResponse result = kingdomStatistic.getKingdomStatistics();

        // Assert
        assertNotNull(result);
        assertEquals(Integer.MAX_VALUE, result.totalKings());
        assertEquals(Integer.MAX_VALUE, result.totalYearlyMinutesSpent());
        assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), result.totalYearlyEarnings());
        assertEquals(Integer.MAX_VALUE, result.maxDailyMinutesSpent());
    }

    @Test
    @DisplayName("Should verify service interactions order")
    void shouldVerifyServiceInteractionsOrder() {
        // Arrange
        when(kingRepositoryPort.getKingdomStatistics()).thenReturn(mockKingdomEntity);
        when(mapper.toKingdomStatisticResponse(mockKingdomEntity)).thenReturn(mockResponse);

        // Act
        kingdomStatistic.getKingdomStatistics();

        // Assert
        var inOrder = inOrder(kingRepositoryPort, mapper);
        inOrder.verify(kingRepositoryPort).getKingdomStatistics();
        inOrder.verify(mapper).toKingdomStatisticResponse(mockKingdomEntity);
        inOrder.verifyNoMoreInteractions();
    }
}
