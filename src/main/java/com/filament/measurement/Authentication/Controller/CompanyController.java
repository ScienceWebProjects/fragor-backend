package com.filament.measurement.Authentication.Controller;

import com.filament.measurement.Authentication.Request.CompanyRequest;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("owner/")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping("company/add/")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Company> addCompany (@Valid @RequestBody CompanyRequest form){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.addCompany(form));
    }

    @GetMapping("company/get/all/")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<Company>> getAllCompany(){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllCompany());
    }
}
