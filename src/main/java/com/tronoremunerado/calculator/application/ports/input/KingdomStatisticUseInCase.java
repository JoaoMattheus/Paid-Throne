package com.tronoremunerado.calculator.application.ports.input;

import com.tronoremunerado.calculator.infrastructure.rest.dto.KingdomStatisticResponse;

public interface KingdomStatisticUseInCase {
    KingdomStatisticResponse getKingdomStatistics();
}
