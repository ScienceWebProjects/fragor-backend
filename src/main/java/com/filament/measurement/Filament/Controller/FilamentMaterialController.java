package com.filament.measurement.Filament.Controller;

import com.filament.measurement.Filament.DTO.FilamentMaterialDTO;
import com.filament.measurement.Filament.Request.FilamentMaterialRequest;
import com.filament.measurement.Filament.Service.FilamentMaterialService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController()
@RequestMapping("api/filaments/material/")
@SuppressWarnings("unused")
public class FilamentMaterialController {
    private final FilamentMaterialService filamentMaterialService;

    public FilamentMaterialController(FilamentMaterialService filamentMaterialService) {
        this.filamentMaterialService = filamentMaterialService;
    }

    @PostMapping("add/")
    @PreAuthorize("hasAuthority('owner:create')")
    public ResponseEntity<Void> addNewFilamentMaterial(
            @Valid
            @RequestBody FilamentMaterialRequest form,
            HttpServletRequest request
    ){
        filamentMaterialService.addNewMaterial(request,form);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @GetMapping("get/all/")
    public ResponseEntity<List<FilamentMaterialDTO>> getAllFilamentsMaterial(){
        return ResponseEntity.status(HttpStatus.OK).body(filamentMaterialService.getAll());
    }
    @DeleteMapping("delete/{id}/")
    @PreAuthorize("hasAuthority('owner:delete')")
    public ResponseEntity<Void> deleteFilamentsMaterial(@PathVariable Long id){
        filamentMaterialService.deleteFilamentMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
