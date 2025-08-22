package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.application.mapper.KingdomStatisticResponseMapper;
import com.tronoremunerado.calculator.application.ports.output.KingRepositoryPort;
import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.RankingEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.RankingKingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private List<RankingEntity> mockRankingEntities;
    private List<RankingKingResponse> mockRankingResponses;

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

        // Setup ranking test data
        mockRankingEntities = Arrays.asList(
            createRankingEntity("king1", 120, BigDecimal.valueOf(50.0)),
            createRankingEntity("king2", 100, BigDecimal.valueOf(45.0)),
            createRankingEntity("king3", 80, BigDecimal.valueOf(40.0))
        );

        mockRankingResponses = Arrays.asList(
            new RankingKingResponse("king1", 120, BigDecimal.valueOf(50.0)),
            new RankingKingResponse("king2", 100, BigDecimal.valueOf(45.0)),
            new RankingKingResponse("king3", 80, BigDecimal.valueOf(40.0))
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
        assertEquals(mockResponse.totalDailyMinutesSpent(), result.totalDailyMinutesSpent());
        assertEquals(mockResponse.totalDailyEarnings(), result.totalDailyEarnings());
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
        assertEquals(0, result.totalDailyMinutesSpent());
        assertEquals(BigDecimal.ZERO, result.totalDailyEarnings());
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
        assertEquals(Integer.MAX_VALUE, result.totalDailyMinutesSpent());
        assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), result.totalDailyEarnings());
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

    // ========== RANKING TESTS ==========

    @Test
    @DisplayName("Should get ranking for higher earnings successfully")
    void shouldGetRankingForHigherEarningsSuccessfully() {
        // Arrange
        RankingType type = RankingType.HIGHER_EARNINGS;
        when(kingRepositoryPort.getRanking(type)).thenReturn(mockRankingEntities);
        when(mapper.toRankingKingResponseList(mockRankingEntities)).thenReturn(mockRankingResponses);

        // Act
        List<RankingKingResponse> result = kingdomStatistic.getRanking(type);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("king1", result.get(0).username());
        assertEquals(120, result.get(0).dailyMinutesSpent());
        assertEquals(BigDecimal.valueOf(50.0), result.get(0).dailyEarnings());

        verify(kingRepositoryPort, times(1)).getRanking(type);
        verify(mapper, times(1)).toRankingKingResponseList(mockRankingEntities);
    }

    @Test
    @DisplayName("Should get ranking for higher minutes successfully")
    void shouldGetRankingForHigherMinutesSuccessfully() {
        // Arrange
        RankingType type = RankingType.HIGHER_MINUTES;
        when(kingRepositoryPort.getRanking(type)).thenReturn(mockRankingEntities);
        when(mapper.toRankingKingResponseList(mockRankingEntities)).thenReturn(mockRankingResponses);

        // Act
        List<RankingKingResponse> result = kingdomStatistic.getRanking(type);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        verify(kingRepositoryPort, times(1)).getRanking(type);
        verify(mapper, times(1)).toRankingKingResponseList(mockRankingEntities);
    }

    @Test
    @DisplayName("Should get ranking for lower minutes successfully")
    void shouldGetRankingForLowerMinutesSuccessfully() {
        // Arrange
        RankingType type = RankingType.LOWER_MINUTES;
        when(kingRepositoryPort.getRanking(type)).thenReturn(mockRankingEntities);
        when(mapper.toRankingKingResponseList(mockRankingEntities)).thenReturn(mockRankingResponses);

        // Act
        List<RankingKingResponse> result = kingdomStatistic.getRanking(type);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        verify(kingRepositoryPort, times(1)).getRanking(type);
        verify(mapper, times(1)).toRankingKingResponseList(mockRankingEntities);
    }

    @Test
    @DisplayName("Should handle empty ranking list")
    void shouldHandleEmptyRankingList() {
        // Arrange
        RankingType type = RankingType.HIGHER_EARNINGS;
        when(kingRepositoryPort.getRanking(type)).thenReturn(Collections.emptyList());
        when(mapper.toRankingKingResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<RankingKingResponse> result = kingdomStatistic.getRanking(type);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(kingRepositoryPort, times(1)).getRanking(type);
        verify(mapper, times(1)).toRankingKingResponseList(Collections.emptyList());
    }

    @Test
    @DisplayName("Should propagate repository exception in ranking")
    void shouldPropagateRepositoryExceptionInRanking() {
        // Arrange
        RankingType type = RankingType.HIGHER_EARNINGS;
        RuntimeException repositoryException = new RuntimeException("Database connection failed");
        when(kingRepositoryPort.getRanking(type)).thenThrow(repositoryException);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kingdomStatistic.getRanking(type);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(kingRepositoryPort, times(1)).getRanking(type);
        verify(mapper, never()).toRankingKingResponseList(any());
    }

    @Test
    @DisplayName("Should propagate mapper exception in ranking")
    void shouldPropagateMapperExceptionInRanking() {
        // Arrange
        RankingType type = RankingType.HIGHER_EARNINGS;
        when(kingRepositoryPort.getRanking(type)).thenReturn(mockRankingEntities);
        when(mapper.toRankingKingResponseList(mockRankingEntities))
                .thenThrow(new RuntimeException("Mapping failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kingdomStatistic.getRanking(type);
        });

        assertEquals("Mapping failed", exception.getMessage());
        verify(kingRepositoryPort, times(1)).getRanking(type);
        verify(mapper, times(1)).toRankingKingResponseList(mockRankingEntities);
    }

    @Test
    @DisplayName("Should verify ranking service interactions order")
    void shouldVerifyRankingServiceInteractionsOrder() {
        // Arrange
        RankingType type = RankingType.HIGHER_EARNINGS;
        when(kingRepositoryPort.getRanking(type)).thenReturn(mockRankingEntities);
        when(mapper.toRankingKingResponseList(mockRankingEntities)).thenReturn(mockRankingResponses);

        // Act
        kingdomStatistic.getRanking(type);

        // Assert
        var inOrder = inOrder(kingRepositoryPort, mapper);
        inOrder.verify(kingRepositoryPort).getRanking(type);
        inOrder.verify(mapper).toRankingKingResponseList(mockRankingEntities);
        inOrder.verifyNoMoreInteractions();
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
