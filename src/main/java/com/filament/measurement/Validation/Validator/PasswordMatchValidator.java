package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Form.ChangeUserPasswordForm;
import com.filament.measurement.Form.UserRegistrationForm;
import com.filament.measurement.Validation.Anotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegistrationForm> {
    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
    }
    @Override
    public boolean isValid(UserRegistrationForm userRegistrationForm, ConstraintValidatorContext constraintValidatorContext) {
        String password = userRegistrationForm.getPassword();
        String password2 = userRegistrationForm.getPassword2();
        if(password.equals(password2)) return true;
        constraintValidatorContext.buildConstraintViolationWithTemplate("Passwords don't match.")
                .addPropertyNode("password2")
                .addConstraintViolation();
        return false;
    }
}
