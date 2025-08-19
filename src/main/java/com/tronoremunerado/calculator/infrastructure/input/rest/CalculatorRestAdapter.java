package com.tronoremunerado.calculator.infrastructure.input.rest;

import com.tronoremunerado.calculator.application.ports.input.CalculateSalaryUseCase;
import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/calculate")
@RequiredArgsConstructor
public class CalculatorRestAdapter {
    private final CalculateSalaryUseCase calculateSalaryUseCase;

    @PostMapping
    public ResponseEntity<KingCalculateResponse> calculateSalary(@RequestBody @Valid King king) {
        return ResponseEntity.ok(calculateSalaryUseCase.calculateSalary(king));
    }
}
