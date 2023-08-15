package com.filament.measurement.Authentication.Controller;

import com.filament.measurement.Authentication.DTO.CompanyDTO;
import com.filament.measurement.Authentication.DTO.UserDTO;
import com.filament.measurement.Authentication.Request.CompanyRequest;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Service.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/companies/")
@CrossOrigin
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("add/")
    public ResponseEntity<Company> addCompany (@Valid @RequestBody CompanyRequest form){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.addCompany(form));
    }
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("get/all/")
    public ResponseEntity<List<CompanyDTO>> getAllCompany(){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllCompany());
    }

    @PreAuthorize("hasAuthority('master:get')")
    @GetMapping("users/get/{id}/")
    public ResponseEntity<List<UserDTO>> getCompanyUsers(@PathVariable Long id, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyUsers(request,id));
    }
}
