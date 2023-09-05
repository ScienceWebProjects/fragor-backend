package com.filament.measurement.Printer.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.DTOMapper.PrinterDTOMapper;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterModel;
import com.filament.measurement.Printer.Repository.PrinterModelRepository;
import com.filament.measurement.Printer.Repository.PrinterRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrinterService {
    @Value("${printer.imagePath}")
    private String printerImagePath;
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

    public void addPrinter(String name, String model, HttpServletRequest request, MultipartFile image) throws IOException {
        Company company = jwtService.extractUser(request).getCompany();
        validatePrinterName(company, name);
        PrinterModel printerModel = getPrinterModel(company,model);
        String imagePath = saveImage(image);
        Printer printer = savePrinterIntoDB(printerModel,name,company,imagePath);
        printerDTOMapper.apply(printer);
    }

    private String saveImage(MultipartFile image) throws IOException {
        if(image.isEmpty()) return "defaultPrinter";
        else if(!Objects.requireNonNull(image.getContentType()).startsWith("image/"))
            throw new CustomValidationException("Invalid file extension");
        String name = LocalDateTime.now().toString();
        image.transferTo(new File(printerImagePath+name));
        return name;
    }
    public byte[] getPrinterImage(String name) throws IOException {
        return Files.readAllBytes(new File(printerImagePath+name).toPath());
    }

    private Printer savePrinterIntoDB(PrinterModel printerModel, String name, Company company, String imagePath) {
        Printer printer = Printer.builder()
                .name(name)
                .company(company)
                .workHours(0.0)
                .printerModel(printerModel)
                .filaments(Collections.emptyList())
                .image(imagePath)
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

    public PrinterDTO get(HttpServletRequest request, Long id) {
        return printerDTOMapper.apply(getPrinter(request,id));
    }

    public void delete(Long id, HttpServletRequest request) {
      printerRepository.delete(getPrinter(request,id));
    }

    public Printer getPrinter(HttpServletRequest request, Long id){
        Company company = jwtService.extractUser(request).getCompany();
        Optional<Printer> printer = printerRepository.findByCompanyAndId(company,id);
        if(printer.isEmpty()) throw new CustomValidationException("Printer doesn't exists.");
        return printer.get();
    }
    private void validatePrinterName(Company company,String name){
        if(name == null || name.equals(""))
            throw new CustomValidationException("Printer's name can't be null.");
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
