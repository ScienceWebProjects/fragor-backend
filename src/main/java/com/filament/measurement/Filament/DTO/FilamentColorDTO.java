package com.filament.measurement.Filament.DTO;

import lombok.Builder;

@Builder
public record FilamentColorDTO(
        Long id,
        String color
) {}
