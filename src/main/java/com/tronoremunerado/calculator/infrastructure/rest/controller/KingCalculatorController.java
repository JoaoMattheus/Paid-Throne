package com.tronoremunerado.calculator.infrastructure.rest.controller;

import com.tronoremunerado.calculator.application.ports.input.CalculateSalaryUseCase;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/calculate")
@RequiredArgsConstructor
@Tag(name = "Bathroom Salary Calculator", description = "API endpoints for calculating bathroom time earnings")
@Slf4j
public class KingCalculatorController {
    private final CalculateSalaryUseCase calculateSalaryUseCase;

    @Operation(
        summary = "Calculate earnings from bathroom time",
        description = "Calculates how much money a person earns during their bathroom breaks based on their salary and time spent"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully calculated bathroom time earnings",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KingCalculateResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input - Total bathroom time exceeds maximum allowed or other validation errors",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<KingCalculateResponse> calculateSalary(
            @Parameter(
                description = "King object containing salary information and bathroom usage details",
                required = true,
                schema = @Schema(implementation = King.class)
            )
            @RequestBody @Valid King king) {
                log.info("Calculating bathroom time earnings for King: {}", king.username());
        return ResponseEntity.ok(calculateSalaryUseCase.calculateSalary(king));
    }
}
