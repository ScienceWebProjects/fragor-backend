package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Device.Repository.AddingDeviceRepository;
import com.filament.measurement.Validation.Anotation.UniqueDeviceName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UniqueDeviceNameValidator implements ConstraintValidator<UniqueDeviceName,String> {
    private final AddingDeviceRepository addingDeviceRepository;
    private final JwtService jwtService;

    public UniqueDeviceNameValidator(AddingDeviceRepository addingDeviceRepository, JwtService jwtService) {
        this.addingDeviceRepository = addingDeviceRepository;
        this.jwtService = jwtService;
    }
    @Override
    public void initialize(UniqueDeviceName constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return !addingDeviceRepository.nameExists(jwtService.extractUser(request).getCompany(),value);
    }
}
