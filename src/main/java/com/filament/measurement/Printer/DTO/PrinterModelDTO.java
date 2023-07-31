package com.filament.measurement.Printer.DTO;

import lombok.Builder;

@Builder
public record PrinterModelDTO(
        Long id,
        String model
) {}
