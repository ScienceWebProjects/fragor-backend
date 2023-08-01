package com.filament.measurement.Filament.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.DTO.FilamentMaterialDTO;
import com.filament.measurement.Filament.DTOMapper.FilamentColorDTOMapper;
import com.filament.measurement.Filament.DTOMapper.FilamentMaterialDTOMapper;
import com.filament.measurement.Filament.Request.FilamentMaterialRequest;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Filament.Repository.FilamentMaterialRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilamentMaterialService {
    private final JwtService jwtService;
    private final FilamentMaterialRepository filamentMaterialRepository;
    private final FilamentMaterialDTOMapper filamentMaterialDTOMapper;

    public FilamentMaterialService(
            JwtService jwtService,
            FilamentMaterialDTOMapper filamentMaterialDTOMapper,
            FilamentMaterialRepository filamentMaterialRepository
    ) {
        this.jwtService = jwtService;
        this.filamentMaterialDTOMapper = filamentMaterialDTOMapper;
        this.filamentMaterialRepository = filamentMaterialRepository;
    }
    public void addNewMaterial(HttpServletRequest request, FilamentMaterialRequest form) {
        User user = jwtService.extractUser(request);
        saveFilamentMaterialIntoDB(form);
    }
    public List<FilamentMaterialDTO> getAll() {
        return filamentMaterialRepository.findAll().stream()
                .map(filamentMaterialDTOMapper)
                .collect(Collectors.toList());
    }
    public void deleteFilamentMaterial(Long id){
        FilamentMaterial filamentMaterial =getFilamentMaterial(id);
        filamentMaterialRepository.delete(filamentMaterial);
    }
    private void saveFilamentMaterialIntoDB(FilamentMaterialRequest form){
        FilamentMaterial filamentMaterial = FilamentMaterial.builder()
                .material(form.getMaterial())
                .hotbed(form.getHotbed())
                .density(form.getDensity())
                .diameter(form.getDiameter())
                .hotend(form.getHotend())
                .build();
        filamentMaterialRepository.save(filamentMaterial);
    }

    private FilamentMaterial getFilamentMaterial(Long id){
        Optional<FilamentMaterial> filamentMaterial = filamentMaterialRepository.findById(id);
        if (filamentMaterial.isEmpty())
            throw new NotFound404Exception("Filament's material not found");
        return filamentMaterial.get();
    }
}
