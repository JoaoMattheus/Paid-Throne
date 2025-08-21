package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.application.mapper.KingdomStatisticResponseMapper;
import com.tronoremunerado.calculator.application.ports.input.KingdomStatisticUseInCase;
import com.tronoremunerado.calculator.application.ports.output.KingRepositoryPort;
import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.RankingEntity;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.RankingKingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KingdomStatistic implements KingdomStatisticUseInCase {
    private final KingRepositoryPort kingRepositoryPort;
    private final KingdomStatisticResponseMapper mapper;

    @Override
    public KingdomStatisticResponse getKingdomStatistics() {
        log.info("Fetching kingdom statistics");
        KingdomEntity stats = kingRepositoryPort.getKingdomStatistics();

        return mapper.toKingdomStatisticResponse(stats);
    }

    @Override
    public List<RankingKingResponse> getRanking(RankingType type) {
        List<RankingEntity> rankingEntities = kingRepositoryPort.getRanking(type);

        return mapper.toRankingKingResponseList(rankingEntities);
    }
}
