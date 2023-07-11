package com.filament.measurement.Controller;

import com.filament.measurement.Form.CompanyForm;
import com.filament.measurement.Model.Company;
import com.filament.measurement.Model.User;
import com.filament.measurement.Repository.CompanyRepository;
import com.filament.measurement.Repository.TokenRepository;
import com.filament.measurement.Repository.UserRepository;
import com.filament.measurement.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("supertajnyendpoint/")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    TokenRepository tokenRepository;

    @GetMapping("get/all/")
    @PreAuthorize("hasRole('changerUser')")
    public User getUsers(HttpServletRequest request){
        User user = jwtService.extractUser(request);
        return user;
    }

    @GetMapping("get/{id}/")
    public Company gestusers(@PathVariable Long id){
        return companyRepository.findById(id).get();
    }
    @PostMapping("addcompany/")
    public ResponseEntity<Company> addCompany (@Valid @RequestBody CompanyForm form){
        Company company = Company.builder()
                .name(form.getName())
                .token(form.getToken())
                .build();
        companyRepository.save(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }
}
