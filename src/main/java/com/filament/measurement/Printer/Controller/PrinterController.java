package com.filament.measurement.Printer.Controller;

import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.Request.PrinterRequest;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Service.PrinterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/printer/")
public class PrinterController {
    private final PrinterService printerService;

    public PrinterController(PrinterService printerService) {
        this.printerService = printerService;
    }

    @PostMapping("add/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<Void> add(@RequestBody PrinterRequest form, HttpServletRequest request){
        printerService.addPrinter(form,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @GetMapping("get/all/")
    public ResponseEntity<List<PrinterDTO>> getAll(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerService.getAll(request));
    }
    @GetMapping("get/{id}/")
    public ResponseEntity<PrinterDTO> get(@PathVariable Long id,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerService.get(request,id));
    }
    @DeleteMapping("delete/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request){
        printerService.delete(id,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
