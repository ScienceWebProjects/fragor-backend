package com.filament.measurement.Filament.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.Form.FilamentMaterialForm;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Filament.Repository.FilamentMaterialRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class FilamentMaterialService {
    @Autowired
    JwtService jwtService;
    @Autowired
    FilamentMaterialRepository filamentMaterialRepository;
    public FilamentMaterial addNewMaterial(HttpServletRequest request, FilamentMaterialForm form) {
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
