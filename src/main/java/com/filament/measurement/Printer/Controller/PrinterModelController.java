package com.filament.measurement.Printer.Controller;

import com.filament.measurement.Printer.DTO.PrinterModelDTO;
import com.filament.measurement.Printer.Request.PrinterModelRequest;
import com.filament.measurement.Printer.Service.PrinterModelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/printer/model/")
@SuppressWarnings("unused")
public class PrinterModelController {
    private final PrinterModelService printerModelService;

    public PrinterModelController(PrinterModelService printerModelService) {
        this.printerModelService = printerModelService;
    }

    @PostMapping("add/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<Void> addPrinterModel(@Valid @RequestBody PrinterModelRequest model, HttpServletRequest request){
        printerModelService.addPrinterModel(model,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @GetMapping("get/all/")
    public ResponseEntity<List<PrinterModelDTO>> getAllPrinterModel(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerModelService.getAllPrinterModels(request));
    }
    @DeleteMapping("delete/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<String> deleteModel(@PathVariable Long id,HttpServletRequest request) {
        try {
            printerModelService.deletePrinterModel(id,request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This model is connected to added printer.");

        }
    }
}
