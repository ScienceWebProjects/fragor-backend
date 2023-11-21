package com.filament.measurement.Validation.Anotation;

import com.filament.measurement.Validation.Validator.UniqueDeviceNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueDeviceNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDeviceName {

    String message() default "Device name already exists.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
