package com.filament.measurement.Printer.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Printer.DTO.PrinterModelDTO;
import com.filament.measurement.Printer.DTOMapper.PrinterModelDTOMapper;
import com.filament.measurement.Printer.Model.PrinterModel;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Printer.Repository.PrinterModelRepository;
import com.filament.measurement.Printer.Request.PrinterModelRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrinterModelService {
    private final JwtService jwtService;
    private final PrinterModelRepository printerModelRepository;
    private final PrinterModelDTOMapper printerModelDTOMapper;

    public PrinterModelService(
            JwtService jwtService,
            PrinterModelRepository printerModelRepository,
            PrinterModelDTOMapper printerModelDTOMapper
    ) {
        this.jwtService = jwtService;
        this.printerModelRepository = printerModelRepository;
        this.printerModelDTOMapper = printerModelDTOMapper;
    }

    public void addPrinterModel(PrinterModelRequest model, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        savePrinterModelIntoDB(user.getCompany(),model.getModel());
    }

    public List<PrinterModelDTO> getAllPrinterModels(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        return printerModelRepository.findAllByCompany(company).stream()
                .map(printerModelDTOMapper)
                .collect(Collectors.toList());
    }

    public void deletePrinterModel(Long id, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        PrinterModel model = getModel(company,id);
        printerModelRepository.delete(model);
    }

    private PrinterModel getModel(Company company , Long id){
        Optional<PrinterModel> model = printerModelRepository.findByCompanyAndId(company,id);
        if(model.isEmpty()) throw new NotFound404Exception("Model doesn't exists.");
        return model.get();
    }

    private void savePrinterModelIntoDB(Company company, String model){
        PrinterModel printerModel = PrinterModel.builder()
                .company(company)
                .model(model)
                .build();
        printerModelRepository.save(printerModel);
    }
}
