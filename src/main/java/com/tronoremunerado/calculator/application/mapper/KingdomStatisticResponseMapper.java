package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import org.springframework.stereotype.Component;

@Component
public class KingdomStatisticResponseMapper {
    public KingdomStatisticResponse toKingdomStatisticResponse(KingdomEntity stats) {
        return new KingdomStatisticResponse(
                stats.getTotalKings(),
                stats.getTotalYearlyMinutesSpent(),
                stats.getTotalYearlyEarnings(),
                stats.getMaxDailyMinutesSpent()
        );
    }
}
