package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingCalculateResponse;

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
