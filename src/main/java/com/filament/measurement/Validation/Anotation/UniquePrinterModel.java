package com.filament.measurement.Validation.Anotation;

import com.filament.measurement.Validation.Validator.UniquePrinterModelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePrinterModelValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePrinterModel {

    String message() default "Printer model already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
