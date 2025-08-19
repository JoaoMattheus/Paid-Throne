package com.tronoremunerado.calculator.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxTotalBathroomTimeValidator.class)
@Documented
public @interface MaxTotalBathroomTime {
    String message() default "Majestade, o tempo total no trono não pode exceder 60 minutos por dia!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
