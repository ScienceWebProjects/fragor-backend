package com.filament.measurement.Validation.Anotation;

import com.filament.measurement.Validation.Validator.UniqueCompanyTokenValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueCompanyTokenValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCompanyToken {
    String message() default "Token already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

