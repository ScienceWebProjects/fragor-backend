package com.filament.measurement.Authentication.Controller;

import com.filament.measurement.Authentication.DTO.CompanyDTO;
import com.filament.measurement.Authentication.DTO.ElectricityTariffDTO;
import com.filament.measurement.Authentication.DTO.UserDTO;
import com.filament.measurement.Authentication.Request.CompanyRequest;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Request.ElectricityTariffRequest;
import com.filament.measurement.Authentication.Service.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/companies/")
@CrossOrigin
@SuppressWarnings("unused")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("add/")
    public ResponseEntity<Company> addCompany(
            @Valid @RequestBody CompanyRequest form
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.addCompany(form));
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("delete/{id}/")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long id
    ) {
        companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("get/all/")
    public ResponseEntity<List<CompanyDTO>> getAllCompany() {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllCompany());
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("users/get/{id}/")
    public ResponseEntity<List<UserDTO>> getCompanyUsersByOwner(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyUsersByOwner(request, id));
    }

    @PreAuthorize("hasAuthority('master:get')")
    @GetMapping("users/get/")
    public ResponseEntity<List<UserDTO>> getCompanyUsersByMaster(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyUsersByMaster(request));
    }

    @PostMapping("settings/electricity/tariff/update/")
    @PreAuthorize("hasAuthority('master:create')")
    public ResponseEntity<Void> addOrUpdateElectricityTariff(
            @Valid @RequestBody ElectricityTariffRequest form,
            HttpServletRequest request
    ) {
        companyService.addOrUpdateElectricityTariff(form, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("settings/electricity/tariff/get/all/")
    public ResponseEntity<List<ElectricityTariffDTO>> getAllElectricityTariff(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllElectricityTariff(request));
    }

    @DeleteMapping("settings/electricity/tariff/delete/{tariff_id}/")
    @PreAuthorize("hasAuthority('master:delete')")
    public ResponseEntity<Void> deleteElectricityTariff(
            @PathVariable Long tariff_id,
            HttpServletRequest request
    ) {
        companyService.deleteElectricityTariff(tariff_id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("settings/electricity/tariff/correct/time/")
    public ResponseEntity<Boolean> correctTime(
            HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.correctTime(request));
    }

}