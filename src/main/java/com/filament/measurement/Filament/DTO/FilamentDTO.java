package com.filament.measurement.Filament.DTO;

import lombok.Builder;

@Builder
public record FilamentDTO(
        double quantity,
        String color,
        String material,
        String brand
)
{}
