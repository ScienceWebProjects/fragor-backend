package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Repository.UserRepository;
import com.filament.measurement.Validation.Anotation.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidate implements ConstraintValidator<UniqueUsername, String> {
    @Autowired
    UserRepository userRepository;
    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
    }
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext){
        return userRepository.findByUsername(username).isEmpty();
    }
}
