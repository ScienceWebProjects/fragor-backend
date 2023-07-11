package com.filament.measurement.Controller;

import com.filament.measurement.Form.FilamentColorForm;
import com.filament.measurement.Model.FilamentColor;
import com.filament.measurement.Service.FilamentColorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/filaments/color/")
public class FilamentColorController {
    @Autowired
    FilamentColorService filamentColorService;

    @GetMapping("get-all-and-add/")
    public ResponseEntity<List<FilamentColor>> getAllCompanyFilamentsColor(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentColorService.getAllColor(request));
    }
    @PostMapping("get-all-and-add/")
    public ResponseEntity<FilamentColor> addNewFilamentsColor(
            @Valid
            @RequestBody FilamentColorForm form,
            HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(filamentColorService.addNewFilamentsColor(form,request));
    }
    @DeleteMapping("delete/{id}/")
    public ResponseEntity<Void> deleteFilamentsColor(@PathVariable Long id, HttpServletRequest request){
        filamentColorService.deleteFilamentColor(id, request);
        return ResponseEntity.noContent().build();
    }
}
