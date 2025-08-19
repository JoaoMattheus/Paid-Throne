package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.application.mapper.CalculationResponseMapper;
import com.tronoremunerado.calculator.application.ports.input.CalculateSalaryUseCase;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculatorService implements CalculateSalaryUseCase {

    private final CalculationResponseMapper mapper;

    @Override
    public KingCalculateResponse calculateSalary(King king) {
        return mapper.toResponse(king);
    }
}
