package com.filament.measurement.Authentication.DTOMapper;

import com.filament.measurement.Authentication.DTO.CompanyDTO;
import com.filament.measurement.Authentication.Model.Company;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CompanyDTOMapper implements Function<Company,CompanyDTO> {
    @Override
    public CompanyDTO apply(Company company) {
        return CompanyDTO.builder()
                .id(company.getId())
                .company(company.getName())
                .token(company.getToken())
                .build();
    }
}
