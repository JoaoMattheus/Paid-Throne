package com.tronoremunerado.calculator.infrastructure.config;

import com.tronoremunerado.calculator.domain.KingEntity;

public interface KingDBConnectionUseInCase {
    void saveKing(KingEntity king);
}
