package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Repository.ElectricityTariffRepository;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Validation.Anotation.UniqueElectricityTariffName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class UniqueElectricityTariffNameValidator implements ConstraintValidator<UniqueElectricityTariffName,String> {
    private final JwtService jwtService;
    private final ElectricityTariffRepository electricityTariffRepository;

    public UniqueElectricityTariffNameValidator(JwtService jwtService, ElectricityTariffRepository electricityTariffRepository) {
        this.jwtService = jwtService;
        this.electricityTariffRepository = electricityTariffRepository;
    }

    @Override
    public void initialize(UniqueElectricityTariffName constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Company company = jwtService.extractUser(request).getCompany();
        return !electricityTariffRepository.nameExists(company,value);
    }
}
