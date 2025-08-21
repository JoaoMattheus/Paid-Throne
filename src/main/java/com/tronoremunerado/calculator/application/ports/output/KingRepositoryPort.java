package com.tronoremunerado.calculator.application.ports.output;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingEntity;
import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingdomEntity;

public interface KingRepositoryPort {
    void saveKing(KingEntity king);

    KingdomEntity getKingdomStatistics();
}
