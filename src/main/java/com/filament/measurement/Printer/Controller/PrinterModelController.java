package com.filament.measurement.Printer.Controller;

import com.filament.measurement.Printer.Model.PrinterModel;
import com.filament.measurement.Printer.Service.PrinterModelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/printer/model/")
public class PrinterModelController {
    @Autowired
    PrinterModelService printerModelService;
    @PostMapping("get-all-and-add/")
    public ResponseEntity<PrinterModel> addPrinterModel(@RequestBody HashMap<String,String> model, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(printerModelService.add(model,request));
    }
    @GetMapping("get-all-and-add/")
    public ResponseEntity<List<PrinterModel>> getAllPrinterModel(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(printerModelService.getAll(request));
    }
    @DeleteMapping("delete/{id}/")
    private ResponseEntity<Void> deleteModel(@PathVariable Long id,HttpServletRequest request) {
        printerModelService.delete(id,request);
        return ResponseEntity.noContent().build();
    }
}
