package com.filament.measurement.Filament.DTOMapper;

import com.filament.measurement.Filament.DTO.FilamentDTO;
import com.filament.measurement.Filament.Model.Filament;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FilamentDTOMapper implements Function<Filament, FilamentDTO> {

    @Override
    public FilamentDTO apply(Filament filament) {
        return new FilamentDTO(
                filament.getId(),
                filament.getQuantity(),
                filament.getColor().getColor(),
                filament.getMaterial().getMaterial(),
                filament.getBrand().getBrand()
                );
    }
}
