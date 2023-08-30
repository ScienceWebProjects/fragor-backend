package com.filament.measurement.Filament.DTOMapper;

;
import com.filament.measurement.Filament.DTO.FilamentBrandDTO;
import com.filament.measurement.Filament.Model.FilamentBrand;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class FilamentBrandDTOMapper implements Function<FilamentBrand, FilamentBrandDTO> {
    @Override
    public FilamentBrandDTO apply(FilamentBrand filamentBrand) {
        return FilamentBrandDTO.builder()
                .id(filamentBrand.getId())
                .brand(filamentBrand.getBrand())
                .build();
    }
}
