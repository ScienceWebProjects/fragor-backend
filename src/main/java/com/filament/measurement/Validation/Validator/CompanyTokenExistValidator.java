package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Repository.CompanyRepository;
import com.filament.measurement.Validation.Anotation.CompanyTokenExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyTokenExistValidator implements ConstraintValidator<CompanyTokenExist,String> {
    @Autowired
    CompanyRepository companyRepository;
    @Override
    public void initialize(CompanyTokenExist constraintAnnotation){}

    @Override
    public boolean isValid(String token, ConstraintValidatorContext constraintValidatorContext){
        return companyRepository.findByToken(token).isPresent();
    }
}
