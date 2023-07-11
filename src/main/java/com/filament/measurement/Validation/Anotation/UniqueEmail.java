package com.filament.measurement.Validation.Anotation;

import com.filament.measurement.Validation.Validator.UniqueEmailValidate;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidate.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email already exist.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
