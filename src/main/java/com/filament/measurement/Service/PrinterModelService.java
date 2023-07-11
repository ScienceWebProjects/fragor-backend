package com.filament.measurement.Service;

import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Model.Company;
import com.filament.measurement.Model.PrinterModel;
import com.filament.measurement.Model.User;
import com.filament.measurement.Repository.PrinterModelRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PrinterModelService {
    @Autowired
    JwtService jwtService;
    @Autowired
    PrinterModelRepository printerModelRepository;
    public PrinterModel add(HashMap<String,String> model, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        if(printerModelRepository.modelExists(user.getCompany(),model.get("model")))
            throw new CustomValidationException("Model already exists.");
        PrinterModel printerModel = PrinterModel.builder()
                .company(user.getCompany())
                .model(model.get("model"))
                .build();
        printerModelRepository.save(printerModel);
        return printerModel;
    }

    public List<PrinterModel> getAll(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        return printerModelRepository.findAllByCompany(company);
    }

    public void delete(Long id, HttpServletRequest request) {
    Company company = jwtService.extractUser(request).getCompany();
    Optional<PrinterModel> model = printerModelRepository.findByCompanyAndId(company,id);
    if(model.isEmpty()) throw new NotFound404Exception("Model doesn't exists.");
    printerModelRepository.delete(model.get());
    }
}
