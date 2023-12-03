package com.filament.measurement.Authentication.DTO;

import lombok.Builder;

@Builder
public record ElectricityTariffDTO(
        Long id,
        Integer hourFrom,
        Integer hourTo,
        double price,
        boolean workingDays,
        boolean weekend,
        String name
) {
}
