package com.filament.measurement.Printer.Controller;

import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.Model.PrinterNotes;
import com.filament.measurement.Printer.Request.PrinterModelRequest;
import com.filament.measurement.Printer.Request.PrinterNotesRequest;
import com.filament.measurement.Printer.Request.PrinterRequest;
import com.filament.measurement.Printer.Service.PrinterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/printer/")
@SuppressWarnings("unused")
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
    ) {
        printerService.addPrinter(request,form);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @PatchMapping("image/{id}/")
    @PreAuthorize("hasAuthority('changer:update')")
    public ResponseEntity<String> updatePrinterImage(@PathVariable Long id, MultipartFile image,HttpServletRequest request) {
        try {
            printerService.updatePrinterImage(id, image, request);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }catch (NoSuchFileException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Image not found");
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
    @PatchMapping("update/{id}/")
    @PreAuthorize("hasAuthority('changer:update')")
    public ResponseEntity<Void> updatePrinterNameModel(
            @PathVariable Long id,
            @Valid @RequestBody PrinterRequest form,
            HttpServletRequest request
    ){
        printerService.updateNameModel(id,form,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("image/{name}/")
    public ResponseEntity<?> getPrinterImage(@PathVariable String name) throws IOException {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(printerService.getPrinterImage(name));
        } catch (NoSuchFileException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Image not found");
        }
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
    @GetMapping("notes/get/all/{printer_id}/")
    public ResponseEntity<List<PrinterNotes>> getAllPrinterNotes (@PathVariable Long printer_id, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerService.getAllPrinterNotes(printer_id,request));
    }

    @PostMapping("notes/update/{printer_id}/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<Void> addOrUpdatePrintersNote(
            @PathVariable Long printer_id,
            HttpServletRequest request,
            @RequestBody PrinterNotesRequest form){
        printerService.addOrUpdateNotes(printer_id,request,form);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("{printer_id}/notes/delete/{note_id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<Void> deletePrinterNotes(
            @PathVariable Long printer_id,
            @PathVariable Long note_id,
            HttpServletRequest request
    ){
        printerService.deleteNotes(printer_id,note_id,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
