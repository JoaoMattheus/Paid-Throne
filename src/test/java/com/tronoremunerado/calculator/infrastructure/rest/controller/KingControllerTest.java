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
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;

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
}
