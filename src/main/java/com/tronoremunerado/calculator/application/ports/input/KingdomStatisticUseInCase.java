package com.tronoremunerado.calculator.application.ports.input;

import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;
import com.tronoremunerado.calculator.infrastructure.rest.dto.RankingKingResponse;

import java.util.List;

public interface KingdomStatisticUseInCase {
    KingdomStatisticResponse getKingdomStatistics();

    List<RankingKingResponse> getRanking(RankingType type);
}
