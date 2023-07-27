package com.filament.measurement.Filament.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.Request.FilamentMaterialRequest;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Filament.Repository.FilamentMaterialRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FilamentMaterialService {
    private final JwtService jwtService;
    private final FilamentMaterialRepository filamentMaterialRepository;
    @Autowired
    public FilamentMaterialService(JwtService jwtService, FilamentMaterialRepository filamentMaterialRepository) {
        this.jwtService = jwtService;
        this.filamentMaterialRepository = filamentMaterialRepository;
    }
    public FilamentMaterial addNewMaterial(HttpServletRequest request, FilamentMaterialRequest form) {
        User user = jwtService.extractUser(request);
        FilamentMaterial filamentMaterial = FilamentMaterial.builder()
                .material(form.getMaterial())
                .hotbed(form.getHotbed())
                .density(form.getDensity())
                .diameter(form.getDiameter())
                .hotend(form.getHotend())
                .build();
        filamentMaterialRepository.save(filamentMaterial);
        return filamentMaterial;
    }
    public void deleteFilamentMaterial(Long id){
        Optional<FilamentMaterial> filamentMaterial = filamentMaterialRepository.findById(id);
        if (filamentMaterial.isEmpty()){
            throw new NotFound404Exception("Filament's material not found");
        }
        filamentMaterialRepository.delete(filamentMaterial.get());
    }
}
