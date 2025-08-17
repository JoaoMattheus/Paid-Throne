package com.tronoremunerado.calculator.controller;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.KingCalculateResponse;
import com.tronoremunerado.calculator.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/calculate")
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService service;

    @PostMapping
    public ResponseEntity<KingCalculateResponse> calculator(@RequestBody King king) {
        return ResponseEntity.ok(service.calculate(king));
    }
}
