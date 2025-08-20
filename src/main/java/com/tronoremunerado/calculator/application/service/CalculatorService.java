package com.tronoremunerado.calculator.application.service;

import com.tronoremunerado.calculator.application.mapper.CalculationResponseMapper;
import com.tronoremunerado.calculator.application.mapper.KingEntityMapper;
import com.tronoremunerado.calculator.application.ports.input.CalculateSalaryUseCase;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.domain.KingEntity;
import com.tronoremunerado.calculator.application.ports.output.KingDBConnectionUseInCase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CalculatorService implements CalculateSalaryUseCase {

    private final CalculationResponseMapper mapperKingResponse;
    private final KingEntityMapper mapperKingEntity;
    private final KingDBConnectionUseInCase kingDBConnection;

    @Override
    public KingCalculateResponse calculateSalary(King king) {

        KingCalculateResponse kingResponse = mapperKingResponse.toResponse(king);
        KingEntity kingEntity = mapperKingEntity.toEntity(kingResponse);

        CompletableFuture.runAsync(() -> {
            kingDBConnection.saveKing(kingEntity);
        });
        return kingResponse;
    }
}
