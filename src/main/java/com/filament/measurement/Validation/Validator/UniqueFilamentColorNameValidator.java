package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Filament.Repository.FilamentColorRepository;
import com.filament.measurement.Validation.Anotation.UniqueFilamentColorName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UniqueFilamentColorNameValidator implements ConstraintValidator<UniqueFilamentColorName,String> {
    private final FilamentColorRepository filamentColorRepository;
    private final JwtService jwtService;
    public UniqueFilamentColorNameValidator(FilamentColorRepository filamentColorRepository, JwtService jwtService) {
        this.filamentColorRepository = filamentColorRepository;
        this.jwtService = jwtService;
    }
    @Override
    public void initialize(UniqueFilamentColorName constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return !filamentColorRepository.colorExists(jwtService.extractUser(request).getCompany(),value);
    }
}