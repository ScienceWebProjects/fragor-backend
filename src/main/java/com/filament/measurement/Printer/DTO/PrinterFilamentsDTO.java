package com.filament.measurement.Printer.DTO;

import lombok.Builder;

@Builder
public record PrinterFilamentsDTO(
        double amount,
        String filamentMaterial
) {
}
