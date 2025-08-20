package com.tronoremunerado.calculator.application.ports.output;

import com.tronoremunerado.calculator.domain.KingEntity;

public interface KingRepositoryPort {
    void saveKing(KingEntity king);
}
