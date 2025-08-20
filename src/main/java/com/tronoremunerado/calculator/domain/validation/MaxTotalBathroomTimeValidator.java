package com.tronoremunerado.calculator.domain.validation;

import com.tronoremunerado.calculator.domain.King;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxTotalBathroomTimeValidator implements ConstraintValidator<MaxTotalBathroomTime, King> {
    @Override
    public void initialize(MaxTotalBathroomTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(King king, ConstraintValidatorContext context) {
        if (king == null) {
            return true;
        }

        int totalBathroomTime = king.averageBathroomTime() * king.numberOfVisitsPerDay();
        return totalBathroomTime <= 60;
    }
}
