package com.filament.measurement.Filament.Controller;

import com.filament.measurement.Filament.DTO.FilamentBrandDTO;
import com.filament.measurement.Filament.Request.FilamentBrandRequest;
import com.filament.measurement.Filament.Service.FilamentBrandService;
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
@RequestMapping("api/filaments/brand/")
public class FilamentBrandController {
    private final FilamentBrandService filamentBrandService;

    public FilamentBrandController(FilamentBrandService filamentBrandService) {
        this.filamentBrandService = filamentBrandService;
    }

    @GetMapping("get/all/")
    public ResponseEntity<List<FilamentBrandDTO>> getAllCompanyFilamentBrand(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentBrandService.getAll(request));
    }
    @PostMapping("add/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<Void> addBrand(
            @Valid
            @RequestBody FilamentBrandRequest form,
            HttpServletRequest request
            ){
        filamentBrandService.addBrand(form,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @DeleteMapping("delete/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<String> deleteBrand(@PathVariable Long id, HttpServletRequest request){
        try {
            filamentBrandService.deleteBrand(id, request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This brand is connected to added filament.");

        }
    }

}
