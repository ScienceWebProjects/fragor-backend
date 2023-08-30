package com.filament.measurement.Filament.DTO;

import lombok.Builder;

@Builder
public record FilamentBrandDTO(
   Long id,
   String brand
) {}
