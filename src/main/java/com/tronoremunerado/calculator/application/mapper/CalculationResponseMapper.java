package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.application.service.BathroomCalculator;
import com.tronoremunerado.calculator.application.service.SalaryCalculator;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.ShiftTime;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculationResponseMapper {

    private final SalaryCalculator salaryCalculator;
    private final BathroomCalculator bathroomCalculator;

    public KingCalculateResponse toResponse(King king) {
        return new KingCalculateResponse(
                king.username(),
                salaryCalculator.calculateMinutesSpent(king, ShiftTime.DAILY),
                salaryCalculator.calculateMinutesSpent(king, ShiftTime.MONTHLY),
                salaryCalculator.calculateMinutesSpent(king, ShiftTime.YEARLY),
                salaryCalculator.calculateTotalEarningsInBathroom(king, ShiftTime.DAILY),
                salaryCalculator.calculateTotalEarningsInBathroom(king, ShiftTime.MONTHLY),
                salaryCalculator.calculateTotalEarningsInBathroom(king, ShiftTime.YEARLY),
                bathroomCalculator.calculateDailyPercentageOfShift(king.averageBathroomTime(), king.numberOfVisitsPerDay(), king.workSchedule().getMinutesWorked(ShiftTime.DAILY))
        );
    }
}
