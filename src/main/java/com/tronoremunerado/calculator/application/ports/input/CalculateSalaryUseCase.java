package com.tronoremunerado.calculator.application.ports.input;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;

public interface CalculateSalaryUseCase {
    KingCalculateResponse calculateSalary(King king);
}
