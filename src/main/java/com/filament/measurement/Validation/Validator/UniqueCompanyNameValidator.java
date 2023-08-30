package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Repository.CompanyRepository;
import com.filament.measurement.Validation.Anotation.UniqueCompanyName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueCompanyNameValidator implements ConstraintValidator<UniqueCompanyName,String> {
    @Autowired
    CompanyRepository companyRepository;
    @Override
    public void initialize(UniqueCompanyName constraintAnnotation){

    }
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext){
        return companyRepository.findByName(name).isEmpty();
    }
}
