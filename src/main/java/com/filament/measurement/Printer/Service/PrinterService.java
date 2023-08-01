package com.filament.measurement.Printer.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.DTOMapper.PrinterDTOMapper;
import com.filament.measurement.Printer.DTOMapper.PrinterModelDTOMapper;
import com.filament.measurement.Printer.Request.PrinterRequest;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterModel;
import com.filament.measurement.Printer.Repository.PrinterModelRepository;
import com.filament.measurement.Printer.Repository.PrinterRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrinterService {
    private final JwtService jwtService;
    private final PrinterRepository printerRepository;
    private final PrinterModelRepository printerModelRepository;
    private final PrinterDTOMapper printerDTOMapper;
    public PrinterService(
            JwtService jwtService,
            PrinterRepository printerRepository,
            PrinterModelRepository printerModelRepository,
            PrinterDTOMapper printerDTOMapper) {
        this.jwtService = jwtService;
        this.printerRepository = printerRepository;
        this.printerModelRepository = printerModelRepository;
        this.printerDTOMapper = printerDTOMapper;
    }

    public PrinterDTO addPrinter(PrinterRequest form, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        validatePrinterName(company, form.getName());
        PrinterModel printerModel = getPrinterModel(company,form.getModel());
        Printer printer = savePrinterIntoDB(printerModel,form.getName(),company);
        return printerDTOMapper.apply(printer);
    }

    private Printer savePrinterIntoDB(PrinterModel printerModel, String name, Company company) {
        Printer printer = Printer.builder()
                .name(name)
                .company(company)
                .workHours(0.0)
                .printerModel(printerModel)
                .build();
        printerRepository.save(printer);
        return printer;
    }

    public List<PrinterDTO> getAll(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        return printerRepository.findAllByCompany(company).stream()
                .map(printerDTOMapper)
                .collect(Collectors.toList());
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
    private void validatePrinterName(Company company,String name){
        if(printerRepository.nameExists(company,name))
            throw new CustomValidationException("Printer with this name already exists.");
    }
    private PrinterModel getPrinterModel(Company company, String model) {
        Optional<PrinterModel> printerModel = printerModelRepository.findByCompanyAndModel(company,model);
        if(printerModel.isEmpty())
            throw new CustomValidationException("Model doesn't exists.");
        return printerModel.get();
    }
}
