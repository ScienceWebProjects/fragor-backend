package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Filament.Repository.FilamentMaterialRepository;
import com.filament.measurement.Validation.Anotation.UniqueFilamentMaterial;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueFilamentMaterialValidate implements ConstraintValidator<UniqueFilamentMaterial, String> {
    @Autowired
    FilamentMaterialRepository filamentMaterialRepository;
    @Override
    public void initialize(UniqueFilamentMaterial constraintAnnotation) {
    }
    @Override
    public boolean isValid(String material, ConstraintValidatorContext constraintValidatorContext){
        return filamentMaterialRepository.findByMaterial(material).isEmpty();
    }
}
