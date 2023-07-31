package com.filament.measurement.Filament.DTO;

import lombok.Builder;

@Builder
public record FilamenMaterialDTO(
        String material,
        String hotbed,
        String hotend,
        double diameter
)
{}
