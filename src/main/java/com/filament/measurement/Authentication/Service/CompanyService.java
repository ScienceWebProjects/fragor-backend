package com.filament.measurement.Authentication.Service;

import com.filament.measurement.Authentication.DTO.CompanyDTO;
import com.filament.measurement.Authentication.DTO.ElectricityTariffDTO;
import com.filament.measurement.Authentication.DTO.UserDTO;
import com.filament.measurement.Authentication.DTOMapper.CompanyDTOMapper;
import com.filament.measurement.Authentication.DTOMapper.ElectricityTariffRequestDTOMapper;
import com.filament.measurement.Authentication.DTOMapper.UserDTOMapper;
import com.filament.measurement.Authentication.Model.ElectricityTariff;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Repository.ElectricityTariffRepository;
import com.filament.measurement.Authentication.Request.CompanyRequest;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Repository.CompanyRepository;
import com.filament.measurement.Authentication.Request.ElectricityTariffRequest;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyDTOMapper companyDTOMapper;
    private final JwtService jwtService;
    private final UserDTOMapper userDTOMapper;
    private final ElectricityTariffRepository electricityTariffRepository;
    private final ElectricityTariffRequestDTOMapper electricityTariffRequestDTOMapper;
    public CompanyService(
            CompanyRepository companyRepository,
            CompanyDTOMapper companyDTOMapper,
            JwtService jwtService,
            UserDTOMapper userDTOMapper,
            ElectricityTariffRepository electricityTariffRepository,
            ElectricityTariffRequestDTOMapper electricityTariffRequestDTOMapper) {
        this.companyRepository = companyRepository;
        this.companyDTOMapper = companyDTOMapper;
        this.jwtService = jwtService;
        this.userDTOMapper = userDTOMapper;
        this.electricityTariffRepository = electricityTariffRepository;
        this.electricityTariffRequestDTOMapper = electricityTariffRequestDTOMapper;
    }

    public Company addCompany (CompanyRequest form){
        String token = createCompanyToken();
        Company company = Company.builder()
                .name(form.getName())
                .token(token)
                .build();
        companyRepository.save(company);
        return company;
    }
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
    public List<CompanyDTO> getAllCompany() {
        return companyRepository.findAll().stream()
                .map(companyDTOMapper)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getCompanyUsersByOwner(HttpServletRequest request, Long id) {
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


    public void addOrUpdateElectricityTariff(ElectricityTariffRequest form, HttpServletRequest request) {
        if(form.getHourFrom()>form.getHourTo())
            throw new CustomValidationException("Hour to must be grater then hour from.");
        Company company = jwtService.extractUser(request).getCompany();
        List<ElectricityTariff> electricityTariffs = getElectricityTariffs(form, company);
        if(!electricityTariffs.isEmpty()) {
            for (ElectricityTariff et : electricityTariffs) {
                if ((form.getHourFrom() >= et.getHourFrom() && form.getHourFrom() < et.getHourTo()) ||
                        form.getHourTo() > et.getHourFrom() && form.getHourTo() <= et.getHourTo())
                    throw new CustomValidationException("Electric tariff with this hours already exists.");
            }
        }
        if (form.getId() == null) {
            addNewElectricityTariff(form, company);
        } else {
            updateElectricityTariff(form, company);
        }
    }


    private List<ElectricityTariff> getElectricityTariffs(ElectricityTariffRequest form, Company company) {
        return electricityTariffRepository.findAllByCompany(company).stream()
                .filter(
                        electricityTariff -> {
                            if (form.isWeekend() && form.isWorkingDays()) {
                                return electricityTariff.isWorkingDays() && electricityTariff.isWeekend();
                            } else if (form.isWorkingDays()) {
                                return electricityTariff.isWorkingDays();
                            } else if (form.isWeekend()) {
                                return electricityTariff.isWeekend();
                            }
                            return false;
                        }).toList();
    }

    public boolean correctTime(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        Optional<ElectricityTariff> electricityTariffOptional = electricityTariffRepository.findByCompanyAndHourFrom(company, 0);
        if(electricityTariffOptional.isEmpty())
            return false;
        ElectricityTariff electricityTariff = electricityTariffOptional.get();

        while(true){
            Optional<ElectricityTariff> byCompanyAndHourFrom = electricityTariffRepository.findByCompanyAndHourFrom(company, electricityTariff.getHourTo());
            if(byCompanyAndHourFrom.isEmpty())
                return false;
            electricityTariff = byCompanyAndHourFrom.get();
            if(electricityTariff.getHourTo() == 24)
                return true;

        }
    }

    private void updateElectricityTariff(ElectricityTariffRequest form, Company company) {
        ElectricityTariff electricityTariff = getElectricityTariff(form.getId(), company);
        electricityTariff.setName(form.getName());
        electricityTariff.setPrice(form.getPrice());
        electricityTariff.setWeekend(form.isWeekend());
        electricityTariff.setWorkingDays(form.isWorkingDays());
        electricityTariff.setHourTo(form.getHourTo());
        electricityTariff.setHourFrom(form.getHourFrom());
        electricityTariffRepository.save(electricityTariff);
    }

    public List<ElectricityTariffDTO> getAllElectricityTariff(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        List<ElectricityTariff> electricityTariffList = electricityTariffRepository.findAllByCompany(company);
        return electricityTariffList.stream()
                .map(electricityTariffRequestDTOMapper)
                .collect(Collectors.toList());
    }

    public void deleteElectricityTariff(Long tariff_id, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        ElectricityTariff electricityTariff = getElectricityTariff(tariff_id, company);
        electricityTariffRepository.delete(electricityTariff);
    }

    private ElectricityTariff getElectricityTariff(Long tariff_id, Company company) {
        Optional<ElectricityTariff> electricityTariffOptional = electricityTariffRepository.findByCompanyAndId(company, tariff_id);
        if(electricityTariffOptional.isEmpty())
            throw new NotFound404Exception("Electricity tariff not found.");
        return electricityTariffOptional.get();
    }

    private void addNewElectricityTariff(ElectricityTariffRequest form, Company company) {
        ElectricityTariff electricityTariff = ElectricityTariff.builder()
                .name(form.getName())
                .hourFrom(form.getHourFrom())
                .hourTo(form.getHourTo())
                .workingDays(form.isWorkingDays())
                .weekend(form.isWeekend())
                .price(form.getPrice())
                .company(company)
                .build();
        electricityTariffRepository.save(electricityTariff);
    }

    private String createCompanyToken() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&";
        Random random = new Random();
        int minLength = 20;
        int maxLength = 30;
        int length = random.nextInt(minLength,maxLength);

        StringBuilder tokenBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            tokenBuilder.append(randomChar);
        }
        String token = tokenBuilder.toString();
        if(companyRepository.tokenExists(token)){
            token = createCompanyToken();
        }
        return token;
    }
}
