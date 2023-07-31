package com.filament.measurement.Filament.DTOMapper;

import com.filament.measurement.Filament.DTO.FilamentColorDTO;
import com.filament.measurement.Filament.Model.FilamentColor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FilamentColorDTOMapper implements Function<FilamentColor, FilamentColorDTO> {
    @Override
    public FilamentColorDTO apply(FilamentColor filamentColor) {
        return FilamentColorDTO.builder()
                .color(filamentColor.getColor())
                .build();
    }
}
