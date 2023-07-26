package com.filament.measurement.Printer.Controller;

import com.filament.measurement.Printer.Form.PrinterForm;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Service.PrinterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/printer/")
public class PrinterController {
    @Autowired
    PrinterService printerService;
    @PostMapping("add/")
    public ResponseEntity<Printer> add(@RequestBody PrinterForm form, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(printerService.add(form,request));
    }
    @GetMapping("get/")
    public ResponseEntity<List<Printer>> getAll(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerService.getAll(request));
    }
    @GetMapping("{id}/")
    public ResponseEntity<Printer> get(@PathVariable Long id,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerService.get(request,id));
    }
    @DeleteMapping("delete/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request){
        printerService.delete(id,request);
        return ResponseEntity.noContent().build();
    }
}