package com.filament.measurement.Printer.Controller;

import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.Request.PrinterRequest;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Service.PrinterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<Void> addPrinter(
            @Valid @RequestBody PrinterRequest form,
            HttpServletRequest request
    ) throws IOException {
        printerService.addPrinter(request,form);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @PatchMapping("image/{id}/")
    public ResponseEntity<Void> updatePrinterImage(@PathVariable Long id, MultipartFile image,HttpServletRequest request) throws IOException {
        printerService.updatePrinterImage(id,image,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @PatchMapping("update/{id}/")
    public ResponseEntity<Void> updatePrinterNameModel(
            @PathVariable Long id,
            @Valid @RequestBody PrinterRequest form,
            HttpServletRequest request
    ){
        printerService.updateNameModel(id,form,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("image/{name}/")
    public ResponseEntity<byte[]> getPrinterImage(@PathVariable String name) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(printerService.getPrinterImage(name));
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
