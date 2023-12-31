package com.filament.measurement.Filament.DTO;

import lombok.Builder;

@Builder
public record FilamentMaterialDTO(
        String material,
        String hotbed,
        String hotend,
        Long id
)
{}
