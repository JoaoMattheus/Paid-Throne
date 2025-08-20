package com.tronoremunerado.calculator.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(
    description = "Response containing calculated earnings and time statistics for bathroom usage",
    name = "KingCalculateResponse"
)
public record KingCalculateResponse(
        @Schema(description = "The username of the King (user)", example = "KingJohn123")
        String username,
        
        @Schema(description = "Total minutes spent in bathroom per day", example = "30")
        int dailyMinutesSpent,
        
        @Schema(description = "Total minutes spent in bathroom per month", example = "600")
        int monthlyMinutesSpent,
        
        @Schema(description = "Total minutes spent in bathroom per year", example = "7200")
        int yearlyMinutesSpent,
        
        @Schema(description = "Amount earned during bathroom time per day", example = "25.00")
        BigDecimal dailyEarnings,
        
        @Schema(description = "Amount earned during bathroom time per month", example = "500.00")
        BigDecimal monthlyEarnings,
        
        @Schema(description = "Amount earned during bathroom time per year", example = "6000.00")
        BigDecimal yearlyEarnings,
        
        @Schema(description = "Percentage of work shift spent in bathroom", example = "6.25")
        double dailyPercentageOfShift
) {}

