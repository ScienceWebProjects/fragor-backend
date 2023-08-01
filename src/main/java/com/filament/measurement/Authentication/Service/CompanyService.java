package com.filament.measurement.Authentication.Service;

import com.filament.measurement.Authentication.Request.CompanyRequest;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company addCompany (CompanyRequest form){
        Company company = Company.builder()
                .name(form.getName())
                .token(form.getToken())
                .build();
        companyRepository.save(company);
        return company;
    }

    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }
}
