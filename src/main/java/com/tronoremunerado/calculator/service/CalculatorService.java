package com.tronoremunerado.calculator.service;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;

public interface CalculatorService {
    KingCalculateResponse calculate(King king);
}
