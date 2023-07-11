package com.filament.measurement.Service;

import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Form.FilamentMaterialForm;
import com.filament.measurement.Model.FilamentMaterial;
import com.filament.measurement.Model.User;
import com.filament.measurement.Repository.FilamentMaterialRepository;
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
