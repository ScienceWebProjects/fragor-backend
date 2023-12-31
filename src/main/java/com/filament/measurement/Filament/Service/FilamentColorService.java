package com.filament.measurement.Filament.Service;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.DTO.FilamentColorDTO;
import com.filament.measurement.Filament.DTOMapper.FilamentColorDTOMapper;
import com.filament.measurement.Filament.Request.FilamentColorRequest;
import com.filament.measurement.Filament.Model.FilamentColor;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Filament.Repository.FilamentColorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilamentColorService {
    private final JwtService jwtService;
    private final FilamentColorRepository filamentColorRepository;
    private final FilamentColorDTOMapper filamentColorDTOMapper;
    public FilamentColorService(
            JwtService jwtService,
            FilamentColorRepository filamentColorRepository,
            FilamentColorDTOMapper filamentColorDTOMapper
    ) {
        this.jwtService = jwtService;
        this.filamentColorRepository = filamentColorRepository;
        this.filamentColorDTOMapper = filamentColorDTOMapper;
    }

    public List<FilamentColorDTO> getAllColor(HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        return filamentColorRepository.findAllByCompanyId(user.getCompany().getId())
                .stream()
                .map(filamentColorDTOMapper)
                .collect(Collectors.toList());
    }

    public void addNewFilamentsColor(
            @RequestBody FilamentColorRequest form,
            HttpServletRequest request
    )
    {
        User user = jwtService.extractUser(request);
        saveNewFilamentIntoDb(user.getCompany(),form.getColor());
    }

    public void deleteFilamentColor(Long id, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        Optional<FilamentColor> filamentColor = filamentColorRepository.findByIdAndCompany(id,user.getCompany());
        if (filamentColor.isEmpty()) throw new NotFound404Exception("Filament color doesn't exists");
        filamentColorRepository.delete(filamentColor.get());
    }

    private void saveNewFilamentIntoDb(Company company,String color){
        filamentColorRepository.save(FilamentColor.builder()
                .color(color)
                .company(company)
                .build());
    }
}
