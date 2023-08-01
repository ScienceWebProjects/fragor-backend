package com.filament.measurement.Filament.Controller;

import com.filament.measurement.Filament.DTO.FilamentDTO;
import com.filament.measurement.Filament.Request.FilamentRequest;
import com.filament.measurement.Filament.Request.FilamentSubtractionRequest;
import com.filament.measurement.Filament.Model.Filament;
import com.filament.measurement.Filament.Service.FilamentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin

@RestController
@RequestMapping("api/filaments/")
public class FilamentController {
    private final  FilamentService filamentService;
    public FilamentController(FilamentService filamentService) {
        this.filamentService = filamentService;
    }

    @PostMapping("add/")
    private ResponseEntity<FilamentDTO> addFilament(@RequestBody FilamentRequest form, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(filamentService.addFilament(form,request));
    }
    @GetMapping("get/all/")
    private ResponseEntity<List<FilamentDTO>> getAllFilaments(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.getAllFilaments(request));
    }
    @GetMapping("get/{id}/")
    private ResponseEntity<FilamentDTO> getFilament (@PathVariable Long id, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.getFilament(id,request));
    }
    @PatchMapping("update/{id}/")
    private ResponseEntity<FilamentDTO> updateFilament(
            @Valid
            @PathVariable Long id,
            HttpServletRequest request,
            @RequestBody FilamentRequest form){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.updateFilament(id,request,form));
    }
    @DeleteMapping("delete/{id}/")
    private ResponseEntity<Void> deleteFilament(@PathVariable Long id,HttpServletRequest request){
        filamentService.deleteFilament(id,request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("filter/{color}/{material}/{quantity}/")
    private ResponseEntity<List<Filament>> getFilteredFilaments(
            @PathVariable String color,
            @PathVariable String material,
            @PathVariable double quantity,
            HttpServletRequest request
    ){
    return ResponseEntity.status(HttpStatus.OK).body(
            filamentService.getFilteredFilament(
                    color,
                    material,
                    quantity,
                    request)
            );

    }
    @PutMapping("subtraction/")
    private ResponseEntity.BodyBuilder subtraction(@RequestBody FilamentSubtractionRequest form, HttpServletRequest request){
        filamentService.subtraction(form,request);
        return ResponseEntity.ok();
    }

    @GetMapping("/add/{amount}/")
    private ResponseEntity<List<Filament>> addRandomFilaments(@PathVariable int amount,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.addRandomFilaments(amount,request));
    }

}
