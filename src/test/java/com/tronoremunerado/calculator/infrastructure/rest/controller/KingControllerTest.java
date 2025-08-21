package com.tronoremunerado.calculator.infrastructure.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tronoremunerado.calculator.application.ports.input.CalculateSalaryUseCase;
import com.tronoremunerado.calculator.application.ports.input.KingdomStatisticUseInCase;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.RankingKingResponse;

@WebMvcTest(KingController.class)
class KingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalculateSalaryUseCase calculateSalaryUseCase;

    @MockBean
    private KingdomStatisticUseInCase kingdomStatisticUseInCase;

    private King validKing;
    private KingCalculateResponse validResponse;

    @BeforeEach
    void setUp() {
        validKing = new King(
                "King123",
                10,
                3,
                BigDecimal.valueOf(50.0),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
        );

        validResponse = new KingCalculateResponse(
                "King123",
                30,
                600,
                7200,
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(600),
                BigDecimal.valueOf(7200),
                50.0
        );
    }

    @Test
    @DisplayName("Should calculate salary successfully with valid input")
    void shouldCalculateSalarySuccessfully() throws Exception {
        // Arrange
        when(calculateSalaryUseCase.calculateSalary(any(King.class))).thenReturn(validResponse);

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validKing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("King123"))
                .andExpect(jsonPath("$.dailyMinutesSpent").value(30))
                .andExpect(jsonPath("$.monthlyMinutesSpent").value(600))
                .andExpect(jsonPath("$.yearlyMinutesSpent").value(7200))
                .andExpect(jsonPath("$.dailyEarnings").value(30))
                .andExpect(jsonPath("$.monthlyEarnings").value(600))
                .andExpect(jsonPath("$.yearlyEarnings").value(7200))
                .andExpect(jsonPath("$.dailyPercentageOfShift").value(50));

        verify(calculateSalaryUseCase).calculateSalary(any(King.class));
    }

    @Test
    @DisplayName("Should return 400 when username is blank")
    void shouldReturn400WhenUsernameIsBlank() throws Exception {
        // Arrange
        King invalidKing = new King(
                "",
                10,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
        );

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidKing)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when average bathroom time is less than 5")
    void shouldReturn400WhenAverageBathroomTimeIsLessThan5() throws Exception {
        // Arrange
        King invalidKing = new King(
                "King123",
                4,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
        );

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidKing)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when number of visits is greater than 5")
    void shouldReturn400WhenNumberOfVisitsIsGreaterThan5() throws Exception {
        // Arrange
        King invalidKing = new King(
                "King123",
                10,
                6,
                BigDecimal.valueOf(1000),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
        );

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidKing)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when salary is greater than 50000")
    void shouldReturn400WhenSalaryIsGreaterThan50000() throws Exception {
        // Arrange
        King invalidKing = new King(
                "King123",
                10,
                3,
                BigDecimal.valueOf(50001),
                SalaryType.HOURLY,
                WorkSchedule.FIVE_ON_TWO
        );

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidKing)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when salary type is null")
    void shouldReturn400WhenSalaryTypeIsNull() throws Exception {
        // Arrange
        King invalidKing = new King(
                "King123",
                10,
                3,
                BigDecimal.valueOf(1000),
                null,
                WorkSchedule.FIVE_ON_TWO
        );

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidKing)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when work schedule is null")
    void shouldReturn400WhenWorkScheduleIsNull() throws Exception {
        // Arrange
        King invalidKing = new King(
                "King123",
                10,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.HOURLY,
                null
        );

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidKing)))
                .andExpect(status().isBadRequest());
    }

    // Novos testes para o endpoint de estatísticas
    @Test
    @DisplayName("Should get kingdom statistics successfully")
    void shouldGetKingdomStatisticsSuccessfully() throws Exception {
        // Arrange
        KingdomStatisticResponse mockResponse = new KingdomStatisticResponse(
                10,
                72000,
                BigDecimal.valueOf(120000.75),
                180
        );

        when(kingdomStatisticUseInCase.getKingdomStatistics()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/v1/statistic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalKings").value(10))
                .andExpect(jsonPath("$.totalYearlyMinutesSpent").value(72000))
                .andExpect(jsonPath("$.totalYearlyEarnings").value(120000.75))
                .andExpect(jsonPath("$.maxDailyMinutesSpent").value(180));

        verify(kingdomStatisticUseInCase, times(1)).getKingdomStatistics();
    }

    @Test
    @DisplayName("Should handle empty kingdom statistics")
    void shouldHandleEmptyKingdomStatistics() throws Exception {
        // Arrange
        KingdomStatisticResponse emptyResponse = new KingdomStatisticResponse(
                0,
                0,
                BigDecimal.ZERO,
                0
        );

        when(kingdomStatisticUseInCase.getKingdomStatistics()).thenReturn(emptyResponse);

        // Act & Assert
        mockMvc.perform(get("/v1/statistic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalKings").value(0))
                .andExpect(jsonPath("$.totalYearlyMinutesSpent").value(0))
                .andExpect(jsonPath("$.totalYearlyEarnings").value(0))
                .andExpect(jsonPath("$.maxDailyMinutesSpent").value(0));

        verify(kingdomStatisticUseInCase, times(1)).getKingdomStatistics();
    }

    @Test
    @DisplayName("Should verify service is called for statistics endpoint")
    void shouldVerifyServiceIsCalledForStatisticsEndpoint() throws Exception {
        // Arrange
        KingdomStatisticResponse mockResponse = new KingdomStatisticResponse(
                1,
                1440,
                BigDecimal.valueOf(1000.0),
                60
        );

        when(kingdomStatisticUseInCase.getKingdomStatistics()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/v1/statistic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(kingdomStatisticUseInCase, times(1)).getKingdomStatistics();
    }

    @Test
    @DisplayName("Should handle large statistics values")
    void shouldHandleLargeStatisticsValues() throws Exception {
        // Arrange
        KingdomStatisticResponse largeResponse = new KingdomStatisticResponse(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                BigDecimal.valueOf(999999999.99),
                1440
        );

        when(kingdomStatisticUseInCase.getKingdomStatistics()).thenReturn(largeResponse);

        // Act & Assert
        mockMvc.perform(get("/v1/statistic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalKings").value(Integer.MAX_VALUE))
                .andExpect(jsonPath("$.totalYearlyMinutesSpent").value(Integer.MAX_VALUE))
                .andExpect(jsonPath("$.totalYearlyEarnings").value(999999999.99))
                .andExpect(jsonPath("$.maxDailyMinutesSpent").value(1440));

        verify(kingdomStatisticUseInCase, times(1)).getKingdomStatistics();
    }

    // ========== RANKING ENDPOINT TESTS ==========

    @Test
    @DisplayName("Should return 200 and ranking list for HIGHER_EARNINGS")
    void shouldReturn200AndRankingListForHigherEarnings() throws Exception {
        // Arrange
        List<RankingKingResponse> mockRanking = Arrays.asList(
            new RankingKingResponse("king1", 120, BigDecimal.valueOf(50.0)),
            new RankingKingResponse("king2", 100, BigDecimal.valueOf(45.0)),
            new RankingKingResponse("king3", 80, BigDecimal.valueOf(40.0))
        );

        when(kingdomStatisticUseInCase.getRanking(RankingType.HIGHER_EARNINGS))
                .thenReturn(mockRanking);

        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .param("type", "HIGHER_EARNINGS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].username").value("king1"))
                .andExpect(jsonPath("$[0].dailyMinutesSpent").value(120))
                .andExpect(jsonPath("$[0].dailyEarnings").value(50.0))
                .andExpect(jsonPath("$[1].username").value("king2"))
                .andExpect(jsonPath("$[2].username").value("king3"));

        verify(kingdomStatisticUseInCase, times(1)).getRanking(RankingType.HIGHER_EARNINGS);
    }

    @Test
    @DisplayName("Should return 200 and ranking list for HIGHER_MINUTES")
    void shouldReturn200AndRankingListForHigherMinutes() throws Exception {
        // Arrange
        List<RankingKingResponse> mockRanking = Arrays.asList(
            new RankingKingResponse("speedKing", 180, BigDecimal.valueOf(30.0)),
            new RankingKingResponse("regularKing", 120, BigDecimal.valueOf(25.0))
        );

        when(kingdomStatisticUseInCase.getRanking(RankingType.HIGHER_MINUTES))
                .thenReturn(mockRanking);

        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .param("type", "HIGHER_MINUTES")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("speedKing"))
                .andExpect(jsonPath("$[0].dailyMinutesSpent").value(180))
                .andExpect(jsonPath("$[1].username").value("regularKing"));

        verify(kingdomStatisticUseInCase, times(1)).getRanking(RankingType.HIGHER_MINUTES);
    }

    @Test
    @DisplayName("Should return 200 and ranking list for LOWER_MINUTES")
    void shouldReturn200AndRankingListForLowerMinutes() throws Exception {
        // Arrange
        List<RankingKingResponse> mockRanking = Arrays.asList(
            new RankingKingResponse("efficientKing", 30, BigDecimal.valueOf(15.0))
        );

        when(kingdomStatisticUseInCase.getRanking(RankingType.LOWER_MINUTES))
                .thenReturn(mockRanking);

        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .param("type", "LOWER_MINUTES")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("efficientKing"))
                .andExpect(jsonPath("$[0].dailyMinutesSpent").value(30));

        verify(kingdomStatisticUseInCase, times(1)).getRanking(RankingType.LOWER_MINUTES);
    }

    @Test
    @DisplayName("Should return 200 and empty array when no ranking data available")
    void shouldReturn200AndEmptyArrayWhenNoRankingDataAvailable() throws Exception {
        // Arrange
        when(kingdomStatisticUseInCase.getRanking(RankingType.HIGHER_EARNINGS))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .param("type", "HIGHER_EARNINGS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(kingdomStatisticUseInCase, times(1)).getRanking(RankingType.HIGHER_EARNINGS);
    }

    @Test
    @DisplayName("Should return 400 when ranking type parameter is missing")
    void shouldReturn400WhenRankingTypeParameterIsMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when ranking type parameter is invalid")
    void shouldReturn400WhenRankingTypeParameterIsInvalid() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .param("type", "INVALID_TYPE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle service exception in ranking endpoint")
    void shouldHandleServiceExceptionInRankingEndpoint() throws Exception {
        // Arrange
        when(kingdomStatisticUseInCase.getRanking(any(RankingType.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .param("type", "HIGHER_EARNINGS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(kingdomStatisticUseInCase, times(1)).getRanking(RankingType.HIGHER_EARNINGS);
    }

    @Test
    @DisplayName("Should handle ranking with decimal earnings precision")
    void shouldHandleRankingWithDecimalEarningsPrecision() throws Exception {
        // Arrange
        List<RankingKingResponse> mockRanking = Arrays.asList(
            new RankingKingResponse("preciseKing", 90, BigDecimal.valueOf(33.4567))
        );

        when(kingdomStatisticUseInCase.getRanking(RankingType.HIGHER_EARNINGS))
                .thenReturn(mockRanking);

        // Act & Assert
        mockMvc.perform(get("/v1/ranking")
                        .param("type", "HIGHER_EARNINGS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dailyEarnings").value(33.4567));

        verify(kingdomStatisticUseInCase, times(1)).getRanking(RankingType.HIGHER_EARNINGS);
    }
}
