package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Printer.Repository.PrinterModelRepository;
import com.filament.measurement.Validation.Anotation.UniquePrinterModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class UniquePrinterModelValidator implements ConstraintValidator<UniquePrinterModel,String> {
    private final JwtService jwtService;
    private final PrinterModelRepository printerModelRepository;

    public UniquePrinterModelValidator(JwtService jwtService, PrinterModelRepository printerModelRepository) {
        this.jwtService = jwtService;
        this.printerModelRepository = printerModelRepository;
    }

    @Override
    public void initialize(UniquePrinterModel constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Company company = jwtService.extractUser(request).getCompany();
        return !printerModelRepository.modelExists(company,value);
    }
}
