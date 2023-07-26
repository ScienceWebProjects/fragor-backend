package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Filament.Repository.FilamentColorRepository;
import com.filament.measurement.Validation.Anotation.UniqueFilamentColor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueFilamentColorValidate implements ConstraintValidator<UniqueFilamentColor, String> {
    @Autowired
    FilamentColorRepository filamentColorRepository;
    @Override
    public void initialize(UniqueFilamentColor constraintAnnotation) {
    }
    @Override
    public boolean isValid(String color, ConstraintValidatorContext constraintValidatorContext){
        return filamentColorRepository.findByColor(color).isEmpty();
    }
}
