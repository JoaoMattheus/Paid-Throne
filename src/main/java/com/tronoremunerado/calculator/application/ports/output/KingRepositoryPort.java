package com.tronoremunerado.calculator.application.ports.output;

import com.tronoremunerado.calculator.infrastructure.persistence.entity.KingEntity;

public interface KingRepositoryPort {
    void saveKing(KingEntity king);
}
