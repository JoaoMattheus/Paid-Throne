package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.KingEntity;
import org.springframework.stereotype.Component;

@Component
public class KingEntityMapper {
    public KingEntity toEntity(KingCalculateResponse kingResponse) {
        return new KingEntity(
                kingResponse.username(),
                kingResponse.dailyMinutesSpent(),
                kingResponse.monthlyMinutesSpent(),
                kingResponse.yearlyMinutesSpent(),
                kingResponse.dailyEarnings(),
                kingResponse.monthlyEarnings(),
                kingResponse.yearlyEarnings(),
                kingResponse.dailyPercentageOfShift()
        );
    }
}
