package com.filament.measurement.Filament.Controller;

import com.filament.measurement.Filament.DTO.FilamentDTO;
import com.filament.measurement.Filament.Request.FilamentRequest;
import com.filament.measurement.Filament.Request.FilamentSubtractionRequest;
import com.filament.measurement.Filament.Service.FilamentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/filaments/")
public class FilamentController {
    private final FilamentService filamentService;

    public FilamentController(FilamentService filamentService) {
        this.filamentService = filamentService;
    }


    @PostMapping("add/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<FilamentDTO> addFilament(@RequestBody FilamentRequest form, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(filamentService.addFilament(form,request));
    }
    @GetMapping("get/all/")
    public ResponseEntity<List<FilamentDTO>> getAllFilaments(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.getAllFilaments(request));
    }

    @GetMapping("get/{id}/")
    public ResponseEntity<FilamentDTO> getFilament (@PathVariable Long id, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.getFilament(id,request));
    }
    @PatchMapping("update/{id}/")
    @PreAuthorize("hasAuthority('changer:update')")
    public ResponseEntity<FilamentDTO> updateFilament(
            @PathVariable Long id,
            HttpServletRequest request,
            @RequestBody FilamentRequest form){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.updateFilament(id,request,form));
    }
    @DeleteMapping("delete/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<Void> deleteFilament(@PathVariable Long id,HttpServletRequest request){
        filamentService.deleteFilament(id,request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("filter/{color}/{material}/{quantity}/")
    public ResponseEntity<List<FilamentDTO>> getFilteredFilaments(
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
    @PreAuthorize("hasAuthority('device:update')")
    public ResponseEntity<Void> subtraction(@RequestBody FilamentSubtractionRequest form, HttpServletRequest request){
        filamentService.subtraction(form,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/add/{amount}/")
    @PreAuthorize("hasAuthority('master:create')")
    public ResponseEntity<Void> addRandomFilaments(@PathVariable int amount,HttpServletRequest request){
        filamentService.addRandomFilaments(amount,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
