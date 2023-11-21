package com.filament.measurement.Authentication.DTO;

import lombok.Builder;

@Builder
public record ElectricityTariffDTO(
        Long id,
        Integer from,
        Integer to,
        double price,
        boolean workingDays,
        boolean weekend,
        String name
) {
}
