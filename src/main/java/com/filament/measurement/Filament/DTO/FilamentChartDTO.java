package com.filament.measurement.Filament.DTO;

import lombok.Builder;

@Builder
public record FilamentChartDTO(
        String color,
        String material,
        String brand,
        double quantity,
        String time
) {}
