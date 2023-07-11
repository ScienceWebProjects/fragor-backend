package com.filament.measurement.Validation.Anotation;

import com.filament.measurement.Validation.Validator.UniqueUsernameValidate;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameValidate.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "Username already exist.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
