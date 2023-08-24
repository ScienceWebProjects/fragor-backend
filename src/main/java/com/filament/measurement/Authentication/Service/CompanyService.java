package com.filament.measurement.Authentication.Service;

import com.filament.measurement.Authentication.DTO.CompanyDTO;
import com.filament.measurement.Authentication.DTO.UserDTO;
import com.filament.measurement.Authentication.DTOMapper.CompanyDTOMapper;
import com.filament.measurement.Authentication.DTOMapper.UserDTOMapper;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Request.CompanyRequest;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Repository.CompanyRepository;
import com.filament.measurement.Exception.CustomValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyDTOMapper companyDTOMapper;
    private final JwtService jwtService;
    private final UserDTOMapper userDTOMapper;
    public CompanyService(
            CompanyRepository companyRepository,
            CompanyDTOMapper companyDTOMapper,
            JwtService jwtService,
            UserDTOMapper userDTOMapper
    ) {
        this.companyRepository = companyRepository;
        this.companyDTOMapper = companyDTOMapper;
        this.jwtService = jwtService;
        this.userDTOMapper = userDTOMapper;
    }

    public Company addCompany (CompanyRequest form){
        Company company = Company.builder()
                .name(form.getName())
                .token(form.getToken())
                .build();
        companyRepository.save(company);
        return company;
    }

    public List<CompanyDTO> getAllCompany() {
        return companyRepository.findAll().stream()
                .map(companyDTOMapper)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getCompanyUsersByOwner(HttpServletRequest request, Long id) {
        User user = jwtService.extractUser(request);
        Optional<Company> companyOptional = companyRepository.findById(id);

        if(companyOptional.isEmpty()) throw new CustomValidationException("Company doesn't exists");
        Company company = companyOptional.get();
        return company.getUsers().stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());

    }

    public List<UserDTO> getCompanyUsersByMaster(HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        return user.getCompany().getUsers().stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }
}
