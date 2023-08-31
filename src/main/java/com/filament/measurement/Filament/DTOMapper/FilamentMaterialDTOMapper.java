package com.filament.measurement.Filament.DTOMapper;

import com.filament.measurement.Filament.DTO.FilamentMaterialDTO;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FilamentMaterialDTOMapper implements Function<FilamentMaterial, FilamentMaterialDTO> {
    @Override
    public FilamentMaterialDTO apply(FilamentMaterial filamentMaterial) {
        return FilamentMaterialDTO.builder()
                .id(filamentMaterial.getId())
                .material(filamentMaterial.getMaterial())
                .hotend(filamentMaterial.getHotend())
                .hotbed(filamentMaterial.getHotbed())
                .build();
    }
}
