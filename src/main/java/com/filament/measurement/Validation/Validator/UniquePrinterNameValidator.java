package com.filament.measurement.Validation.Validator;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Printer.Repository.PrinterRepository;
import com.filament.measurement.Validation.Anotation.UniquePrinterName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class UniquePrinterNameValidator implements ConstraintValidator<UniquePrinterName,String> {
    private final JwtService jwtService;
    private final PrinterRepository printerRepository;

    public UniquePrinterNameValidator(JwtService jwtService, PrinterRepository printerRepository) {
        this.jwtService = jwtService;
        this.printerRepository = printerRepository;
    }

    @Override
    public void initialize(UniquePrinterName constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Company company = jwtService.extractUser(request).getCompany();
        return !printerRepository.nameExists(company,value);
    }
}
