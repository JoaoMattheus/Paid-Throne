package com.tronoremunerado.calculator.application.ports.output;

import com.tronoremunerado.calculator.domain.RankingType;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.RankingEntity;

import java.util.List;

public interface KingRepositoryPort {
    void saveKing(KingEntity king);

    KingdomEntity getKingdomStatistics();

    List<RankingEntity> getRanking(RankingType type);
}
