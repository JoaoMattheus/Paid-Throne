package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.application.service.SalaryCalculator;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.ShiftTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculationResponseMapper {

    private final SalaryCalculator salaryCalculator;

    public KingCalculateResponse toResponse(King king) {
        return new KingCalculateResponse(
                king.username(),
                salaryCalculator.calculateMinutesSpent(king, ShiftTime.DAILY),
                salaryCalculator.calculateMinutesSpent(king, ShiftTime.MONTHLY),
                salaryCalculator.calculateMinutesSpent(king, ShiftTime.YEARLY),
                salaryCalculator.calculateEarningsPerMinute(king, ShiftTime.DAILY),
                salaryCalculator.calculateEarningsPerMinute(king, ShiftTime.MONTHLY),
                salaryCalculator.calculateEarningsPerMinute(king, ShiftTime.YEARLY),
                king.salary()
        );
    }
}
