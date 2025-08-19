package com.tronoremunerado.calculator.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar um King válido com sucesso")
    void shouldCreateValidKing() {
        King king = new King(
                "Rei12345",
                10,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve validar nome do rei vazio")
    void shouldValidateEmptyUsername() {
        King king = new King(
                "",
                10,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertEquals("Não é de bom tom um rei não se apresentar!", violations.iterator().next().getMessage());
    }

    @ParameterizedTest
    @DisplayName("Deve validar nomes de rei inválidos")
    @ValueSource(strings = {"Rei@", "Rei#", "Rei$", "Rei%"})
    void shouldValidateInvalidUsernameCharacters(String username) {
        King king = new King(
                username,
                10,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Majestade, seu nome não é esse, não é mesmo? Ele deve conter apenas letras e números.")));
    }

    @Test
    @DisplayName("Deve validar tempo mínimo no banheiro")
    void shouldValidateMinimumBathroomTime() {
        King king = new King(
                "Rei12345",
                4,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Majestade, não precisa ser tão modesto, mas seu tempo no trono não deve ser menor que 5 minutos.")));
    }

    @Test
    @DisplayName("Deve validar número mínimo de visitas diárias")
    void shouldValidateMinimumVisitsPerDay() {
        King king = new King(
                "Rei12345",
                10,
                0,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Majestade, você deve visitar o trono pelo menos uma vez por dia.")));
    }

    @Test
    @DisplayName("Deve validar número máximo de visitas diárias")
    void shouldValidateMaximumVisitsPerDay() {
        King king = new King(
                "Rei12345",
                10,
                6,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Tragam a coroa! Eis aqui um rei que não larga o trono! Você não pode visitar o trono mais de 5 vezes por dia.")));
    }

    @Test
    @DisplayName("Deve validar salário mínimo")
    void shouldValidateMinimumSalary() {
        King king = new King(
                "Rei12345",
                10,
                3,
                BigDecimal.ZERO,
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Majestade, você deve ter um salário digno de sua realeza.")));
    }

    @Test
    @DisplayName("Deve validar salário máximo")
    void shouldValidateMaximumSalary() {
        King king = new King(
                "Rei12345",
                10,
                3,
                BigDecimal.valueOf(50001),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Magestade, sabemos que você é rico, mas seu salário não deve ser maior que 50.000 moedas.")));
    }

    @Test
    @DisplayName("Deve validar tipo de salário vazio")
    void shouldValidateEmptySalaryType() {
        King king = new King(
                "Rei12345",
                10,
                3,
                BigDecimal.valueOf(1000),
                null,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Majestade, você deve escolher um tipo de salário.")));
    }

    @Test
    @DisplayName("Deve validar horário de trabalho vazio")
    void shouldValidateEmptyWorkSchedule() {
        King king = new King(
                "Rei12345",
                10,
                3,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                null
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Majestade, você deve escolher um horário de trabalho.")));
    }

    @Test
    @DisplayName("Deve validar tempo total no banheiro maior que uma hora")
    void shouldValidateTotalBathroomTimeExceedingOneHour() {
        King king = new King(
                "Rei12345",
                15,
                5,
                BigDecimal.valueOf(1000),
                SalaryType.MONTHLY,
                WorkSchedule.FIVE_ON_TWO
        );

        Set<ConstraintViolation<King>> violations = validator.validate(king);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Vossa majestade deveria caminhar mais! Ficar mais de uma hora no trono não é saudável!")));
    }
}
