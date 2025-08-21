package com.tronoremunerado.calculator.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.tronoremunerado.calculator.domain.King;
import com.tronoremunerado.calculator.domain.ShiftTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SalaryCalculator {

    public int calculateMinutesSpent(King king, ShiftTime shiftTime) {
        log.info("Calculating minutes spent for user {} in period {}", king.username(), shiftTime);
        
        int minutesOnBathroomPerDay = king.averageBathroomTime() * king.numberOfVisitsPerDay();
        int result = switch (shiftTime) {
            case DAILY -> minutesOnBathroomPerDay;
            case MONTHLY -> minutesOnBathroomPerDay * king.workSchedule().getDaysPerMonth();
            case YEARLY -> minutesOnBathroomPerDay * king.workSchedule().getDaysPerYear();
        };

        log.info("Minutes spent calculated for user {} in period {}: {} minutes", king.username(), shiftTime, result);
        return result;
    }

    /**
     * Calcula o valor ganho por minuto baseado no tipo de salário.
     * 
     * @param king o usuário para calcular
     * @return valor ganho por minuto de trabalho
     */
    public BigDecimal calculateSalaryPerMinute(King king) {
        log.info("Calculating salary per minute for user {} - salary type: {}", 
                 king.username(), king.salaryType());
        
        BigDecimal salaryPerMinute = switch (king.salaryType()) {
            case HOURLY -> king.salary().divide(BigDecimal.valueOf(60), 2, RoundingMode.DOWN);
            case DAILY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.DAILY)), 2, RoundingMode.DOWN);
            case MONTHLY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.MONTHLY)), 2, RoundingMode.DOWN);
            case YEARLY -> king.salary().divide(BigDecimal.valueOf(king.workSchedule().getMinutesWorked(ShiftTime.YEARLY)), 2, RoundingMode.DOWN);
        };

        log.info("Salary per minute calculated: {}", salaryPerMinute);
        return salaryPerMinute;
    }

    /**
     * Calcula o valor total ganho durante o tempo passado no banheiro em um período específico.
     * 
     * @param king o usuário para calcular
     * @param shiftTime o período (diário, mensal ou anual)
     * @return valor total ganho durante o tempo no banheiro no período especificado
     */
    public BigDecimal calculateTotalEarningsInBathroom(King king, ShiftTime shiftTime) {
        log.info("Calculating total earnings in bathroom for user {} - salary type: {}, period: {}", 
                 king.username(), king.salaryType(), shiftTime);
        
        // Usa o método auxiliar para obter o valor por minuto
        BigDecimal salaryPerMinute = calculateSalaryPerMinute(king);

        // Calcula o total de minutos gastos no banheiro no período
        int minutesSpentInBathroom = calculateMinutesSpent(king, shiftTime);
        
        // Multiplica valor por minuto pelos minutos gastos no banheiro
        BigDecimal totalEarnings = salaryPerMinute.multiply(BigDecimal.valueOf(minutesSpentInBathroom));

        log.info("Total earnings in bathroom calculated for {}: {}", shiftTime, totalEarnings);
        return totalEarnings;
    }
}
