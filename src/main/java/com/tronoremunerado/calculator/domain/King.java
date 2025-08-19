package com.tronoremunerado.calculator.domain;

import com.tronoremunerado.calculator.domain.validation.MaxTotalBathroomTime;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@MaxTotalBathroomTime(message = "Vossa majestade deveria caminhar mais! Ficar mais de uma hora no trono não é saudável!")
public record King(
        @NotBlank(message = "Não é de bom tom um rei não se apresentar!")
        @Size(min = 5, message = "Majestade, seu nome deve ter pelo menos 5 caracteres.")
        @Size(max = 15, message = "Majestade, seu nome não pode ter mais de 15 caracteres.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Majestade, seu nome não é esse, não é mesmo? Ele deve conter apenas letras e números.")
        String username,

        @Min(value = 5, message = "Majestade, não precisa ser tão modesto, mas seu tempo no trono não deve ser menor que 5 minutos.")
        int averageBathroomTime,

        @Min(value = 1, message = "Majestade, você deve visitar o trono pelo menos uma vez por dia.")
        @Max(value = 5, message = "Tragam a coroa! Eis aqui um rei que não larga o trono! Você não pode visitar o trono mais de 5 vezes por dia.")
        int numberOfVisitsPerDay,

        @Min(value = 1, message = "Majestade, você deve ter um salário digno de sua realeza.")
        @Max(value = 50000, message = "Magestade, sabemos que você é rico, mas seu salário não deve ser maior que 50.000 moedas.")
        BigDecimal salary,

        @NotNull(message = "Majestade, você deve escolher um tipo de salário.")
        SalaryType salaryType,

        @NotNull(message = "Majestade, você deve escolher um horário de trabalho.")
        WorkSchedule workSchedule
) {}
