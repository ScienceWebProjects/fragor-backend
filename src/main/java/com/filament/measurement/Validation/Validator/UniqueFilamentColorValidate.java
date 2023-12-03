package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Filament.Repository.FilamentColorRepository;
import com.filament.measurement.Validation.Anotation.UniqueFilamentColor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueFilamentColorValidate implements ConstraintValidator<UniqueFilamentColor, String> {
    private final FilamentColorRepository filamentColorRepository;

    public UniqueFilamentColorValidate(FilamentColorRepository filamentColorRepository) {
        this.filamentColorRepository = filamentColorRepository;
    }

    @Override
    public void initialize(UniqueFilamentColor constraintAnnotation) {
    }
    @Override
    public boolean isValid(String color, ConstraintValidatorContext constraintValidatorContext){
        return filamentColorRepository.findByColor(color).isEmpty();
    }
}