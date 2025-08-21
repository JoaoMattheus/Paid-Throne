package com.tronoremunerado.calculator.infrastructure.rest.controller;

import com.tronoremunerado.calculator.application.ports.input.CalculateSalaryUseCase;
import com.tronoremunerado.calculator.application.ports.input.KingdomStatisticUseInCase;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;

import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.RankingKingResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Bathroom Salary Calculator", description = "API endpoints for calculating bathroom time earnings")
@Slf4j
public class KingController {
    private final CalculateSalaryUseCase calculateSalaryUseCase;
    private final KingdomStatisticUseInCase kingdonStatisticUseInCase;

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
    @PostMapping("/calculate")
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

    @GetMapping("/statistic")
    @CrossOrigin(origins = "*")
    @Operation(
        summary = "Get kingdom statistics",
        description = "Retrieves aggregated statistics about all kings in the kingdom"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved kingdom statistics",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KingdomStatisticResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    public ResponseEntity<KingdomStatisticResponse> getStatistics() {
        log.info("Fetching kingdom statistics");
        KingdomStatisticResponse stats = kingdonStatisticUseInCase.getKingdomStatistics();
        log.info("Kingdom statistics retrieved: {}", stats);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/ranking")
    @CrossOrigin(origins = "*")
    @Operation(
        summary = "Get kingdom ranking",
        description = "Retrieves ranking of kings based on the specified type (earnings or bathroom time)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved ranking",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RankingKingResponse.class, type = "array")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid ranking type parameter",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content
        )
    })
    public ResponseEntity<List<RankingKingResponse>> getRanking(
            @Parameter(
                description = "Type of ranking to retrieve",
                required = true,
                schema = @Schema(implementation = RankingType.class)
            )
            @RequestParam RankingType type) {
        try {
            log.info("Fetching ranking statistics for type: {}", type);
            List<RankingKingResponse> ranking = kingdonStatisticUseInCase.getRanking(type);
            log.info("Successfully retrieved {} ranking entries", ranking.size());
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            log.error("Error fetching ranking for type: {}", type, e);
            return ResponseEntity.status(500).build();
        }
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.OPTIONS)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> handleStatisticOptions() {
        log.debug("Handling OPTIONS request for /statistic endpoint");
        return ResponseEntity.ok().build();
    }
}
