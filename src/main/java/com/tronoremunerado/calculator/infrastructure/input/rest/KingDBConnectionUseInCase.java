package com.tronoremunerado.calculator.infrastructure.input.rest;

import com.tronoremunerado.calculator.domain.KingEntity;

public interface KingDBConnectionUseInCase {
    void saveKing(KingEntity king);
}
