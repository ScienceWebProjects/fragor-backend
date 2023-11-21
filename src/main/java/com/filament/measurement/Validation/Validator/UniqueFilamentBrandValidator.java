package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Device.Repository.AddingDeviceRepository;
import com.filament.measurement.Filament.Repository.FilamentBrandRepository;
import com.filament.measurement.Filament.Service.FilamentBrandService;
import com.filament.measurement.Validation.Anotation.UniqueDeviceName;
import com.filament.measurement.Validation.Anotation.UniqueFilamentBrand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class UniqueFilamentBrandValidator implements ConstraintValidator<UniqueFilamentBrand,String> {
    private final FilamentBrandRepository filamentBrandRepository;
    private final JwtService jwtService;

    public UniqueFilamentBrandValidator(FilamentBrandRepository filamentBrandRepository, JwtService jwtService) {
        this.filamentBrandRepository = filamentBrandRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void initialize(UniqueFilamentBrand constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Company company = jwtService.extractUser(request).getCompany();
        return !filamentBrandRepository.brandExists(company, value);
    }
}
