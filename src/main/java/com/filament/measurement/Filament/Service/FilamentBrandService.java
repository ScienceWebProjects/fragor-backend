package com.filament.measurement.Filament.Service;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.DTO.FilamentBrandDTO;
import com.filament.measurement.Filament.DTOMapper.FilamentBrandDTOMapper;
import com.filament.measurement.Filament.Model.FilamentBrand;
import com.filament.measurement.Filament.Repository.FilamentBrandRepository;
import com.filament.measurement.Filament.Request.FilamentBrandRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilamentBrandService {
    private final FilamentBrandRepository filamentBrandRepository;
    private final JwtService jwtService;
    private final FilamentBrandDTOMapper filamentBrandDTOMapper;

    public FilamentBrandService(
            FilamentBrandRepository filamentBrandRepository,
            JwtService jwtService,
            FilamentBrandDTOMapper filamentBrandDTOMapper
    ) {
        this.filamentBrandRepository = filamentBrandRepository;
        this.jwtService = jwtService;
        this.filamentBrandDTOMapper = filamentBrandDTOMapper;
    }

    public List<FilamentBrandDTO> getAll(HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        return filamentBrandRepository.findAllByCompanyId(user.getCompany().getId()).stream()
                .map(filamentBrandDTOMapper)
                .collect(Collectors.toList());
    }

    public void addBrand(FilamentBrandRequest form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        filamentBrandDoesNotExists(form,user.getCompany());
        saveBrandIntoDB(user.getCompany(),form);
    }

    private void saveBrandIntoDB(Company company, FilamentBrandRequest form) {
        filamentBrandRepository.save(
                FilamentBrand.builder()
                .name(form.getBrand())
                .company(company)
                .build()
        );
    }

    private void filamentBrandDoesNotExists(FilamentBrandRequest form, Company company) {
        if(filamentBrandRepository.brandExists(company, form.getBrand()))
            throw new CustomValidationException("Brand already exists.");
    }

    public void deleteBrand(Long id, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        Optional<FilamentBrand> filamentBrandOptional = filamentBrandRepository.findByCompanyAndId(user.getCompany(),id);
        if(filamentBrandOptional.isEmpty())
            throw new NotFound404Exception("Brand doesn't exists.");
        filamentBrandRepository.delete(filamentBrandOptional.get());
    }
}
