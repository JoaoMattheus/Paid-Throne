package com.tronoremunerado.calculator.application.mapper;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.RankingEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.RankingKingResponse;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<RankingKingResponse> toRankingKingResponseList(List<RankingEntity> rankingEntities) {
        return rankingEntities.stream()
                .map(entity -> new RankingKingResponse(
                        entity.getUsername(),
                        entity.getDailyMinutesSpent(),
                        entity.getDailyEarnings()
                ))
                .toList();
    }
}
