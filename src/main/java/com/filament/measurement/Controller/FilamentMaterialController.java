package com.filament.measurement.Controller;

import com.filament.measurement.Form.FilamentMaterialForm;
import com.filament.measurement.Model.FilamentMaterial;
import com.filament.measurement.Repository.FilamentMaterialRepository;
import com.filament.measurement.Service.FilamentMaterialService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/filaments/material/")
public class FilamentMaterialController {
    @Autowired
    FilamentMaterialService filamentMaterialService;
    @Autowired
    FilamentMaterialRepository filamentMaterialRepository;
    @PostMapping("get-all-and-add/")
    public ResponseEntity<FilamentMaterial> addNewFilamentMaterial(
            @Valid
            @RequestBody FilamentMaterialForm form,
            HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(filamentMaterialService.addNewMaterial(request,form));
    }
    @GetMapping("get-all-and-add/")
    public ResponseEntity<List<FilamentMaterial>> getAllFilamentsMaterial(){
        return ResponseEntity.status(HttpStatus.OK).body(filamentMaterialRepository.findAll());
    }
    @DeleteMapping("delete/{id}/")
    public ResponseEntity<Void> deleteFilamentsMaterial(@PathVariable Long id){
        filamentMaterialService.deleteFilamentMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
