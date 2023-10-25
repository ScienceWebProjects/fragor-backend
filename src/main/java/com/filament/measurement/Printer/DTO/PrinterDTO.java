package com.filament.measurement.Printer.DTO;

import lombok.Builder;

import java.util.List;
@Builder
public record PrinterDTO (
        long id,
        String name,
        double workHours,
        String model,
        String image,
        List<PrinterFilamentsDTO> filaments
)
{}
