package com.filament.measurement.Filament.Controller;

import com.filament.measurement.Filament.DTO.FilamentColorDTO;
import com.filament.measurement.Filament.Request.FilamentColorRequest;
import com.filament.measurement.Filament.Model.FilamentColor;
import com.filament.measurement.Filament.Service.FilamentColorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/filaments/color/")
@CrossOrigin
public class FilamentColorController {
    private final FilamentColorService filamentColorService;

    public FilamentColorController(FilamentColorService filamentColorService) {
        this.filamentColorService = filamentColorService;
    }

    @GetMapping("get/all/")
    public ResponseEntity<List<FilamentColorDTO>> getAllCompanyFilamentsColor(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentColorService.getAllColor(request));
    }
    @PostMapping("add/")
    public ResponseEntity<Void> addNewFilamentsColor(
            @Valid
            @RequestBody FilamentColorRequest form,
            HttpServletRequest request
    ){
        filamentColorService.addNewFilamentsColor(form,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @DeleteMapping("delete/{id}/")
    public ResponseEntity<Void> deleteFilamentsColor(@PathVariable Long id, HttpServletRequest request){
        filamentColorService.deleteFilamentColor(id, request);
        return ResponseEntity.noContent().build();
    }
}
