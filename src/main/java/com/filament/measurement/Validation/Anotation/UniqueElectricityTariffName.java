package com.filament.measurement.Validation.Anotation;

import com.filament.measurement.Validation.Validator.UniqueElectricityTariffNameValidator;
import com.filament.measurement.Validation.Validator.UniquePrinterModelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueElectricityTariffNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueElectricityTariffName {

    String message() default "Electricity tariff name already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
