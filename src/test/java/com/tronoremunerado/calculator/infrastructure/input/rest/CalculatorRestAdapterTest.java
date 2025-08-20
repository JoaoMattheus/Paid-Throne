package com.tronoremunerado.calculator.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tronoremunerado.calculator.application.ports.input.CalculateSalaryUseCase;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.SalaryType;
import com.tronoremunerado.calculator.domain.WorkSchedule;
import com.tronoremunerado.calculator.infrastructure.rest.controller.KingCalculatorController;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KingCalculatorController.class)
class CalculatorRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalculateSalaryUseCase calculateSalaryUseCase;

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
                "",  // invalid blank username
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
                4,  // invalid bathroom time
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
                6,  // invalid number of visits
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
                BigDecimal.valueOf(50001),  // invalid salary
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
                null,  // invalid salary type
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
                null  // invalid work schedule
        );

        // Act & Assert
        mockMvc.perform(post("/v1/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidKing)))
                .andExpect(status().isBadRequest());
    }
}
