package com.filament.measurement.Printer.Controller;

import com.filament.measurement.Printer.DTO.PrinterModelDTO;
import com.filament.measurement.Printer.Model.PrinterModel;
import com.filament.measurement.Printer.Request.PrinterModelRequest;
import com.filament.measurement.Printer.Service.PrinterModelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@CrossOrigin

@RestController
@RequestMapping("/api/printer/model/")
public class PrinterModelController {
    private final PrinterModelService printerModelService;

    public PrinterModelController(PrinterModelService printerModelService) {
        this.printerModelService = printerModelService;
    }

    @PostMapping("add/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<Void> addPrinterModel(@RequestBody PrinterModelRequest model, HttpServletRequest request){
        printerModelService.add(model,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @GetMapping("get/all/")
    public ResponseEntity<List<PrinterModelDTO>> getAllPrinterModel(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerModelService.getAll(request));
    }
    @DeleteMapping("delete/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    private ResponseEntity<Void> deleteModel(@PathVariable Long id,HttpServletRequest request) {
        printerModelService.delete(id,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
