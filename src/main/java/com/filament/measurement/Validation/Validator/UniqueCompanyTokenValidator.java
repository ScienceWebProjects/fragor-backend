package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Repository.CompanyRepository;
import com.filament.measurement.Validation.Anotation.UniqueCompanyToken;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueCompanyTokenValidator implements ConstraintValidator<UniqueCompanyToken,String> {
    @Autowired
    CompanyRepository companyRepository;
    @Override
    public void initialize(UniqueCompanyToken constraintAnnotation){

    }
    @Override
    public boolean isValid(String token, ConstraintValidatorContext constraintValidatorContext){
        return companyRepository.findByToken(token).isEmpty();
    }
}
