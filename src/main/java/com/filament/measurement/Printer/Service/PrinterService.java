package com.filament.measurement.Printer.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Printer.Form.PrinterForm;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterModel;
import com.filament.measurement.Printer.Repository.PrinterModelRepository;
import com.filament.measurement.Printer.Repository.PrinterRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrinterService {
    @Autowired
    JwtService jwtService;
    @Autowired
    PrinterRepository printerRepository;
    @Autowired
    PrinterModelRepository printerModelRepository;
    public Printer add(PrinterForm form, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        if(printerRepository.nameExists(company,form.getName()))
            throw new CustomValidationException("Printer with this name already exists.");
        Optional<PrinterModel> printerModel = printerModelRepository.findByCompanyAndModel(company,form.getModel());
        if(printerModel.isEmpty())
            throw new CustomValidationException("Model doesn't exists.");
        Printer printer = Printer.builder()
                .name(form.getName())
                .company(company)
                .workHours(0.0)
                .printerModel(printerModel.get())
                .build();
        printerRepository.save(printer);
        return printer;
    }

    public List<Printer> getAll(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        return printerRepository.findAllByCompany(company);
    }

    public Printer get(HttpServletRequest request, Long id) {
        return getPrinter(request,id);
    }

    public void delete(Long id, HttpServletRequest request) {
      printerRepository.delete(getPrinter(request,id));
    }
    private Printer getPrinter(HttpServletRequest request, Long id){
        Company company = jwtService.extractUser(request).getCompany();
        Optional<Printer> printer = printerRepository.findByCompanyAndId(company,id);
        if(printer.isEmpty()) throw new CustomValidationException("Printer doesn't exists.");
        return printer.get();
    }
}
