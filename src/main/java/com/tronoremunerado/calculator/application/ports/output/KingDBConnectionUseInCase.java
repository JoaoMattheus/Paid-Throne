package com.tronoremunerado.calculator.application.ports.output;

import com.tronoremunerado.calculator.domain.KingEntity;

public interface KingDBConnectionUseInCase {
    void saveKing(KingEntity king);
}
