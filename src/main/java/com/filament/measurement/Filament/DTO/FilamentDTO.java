package com.filament.measurement.Filament.DTO;

import lombok.Builder;

@Builder
public record FilamentDTO(
        Long id,
        double quantity,
        String color,
        String material,
        String brand,
        double diameter
)
{}
