package com.filament.measurement.Printer.DTO;

import com.filament.measurement.Device.Model.Device;
import com.filament.measurement.Printer.Model.PrinterFilaments;
import lombok.Builder;

import java.util.List;
@Builder
public record PrinterDTO (
        long id,
        String name,
        double workHours,
        String model,
        List<PrinterFilamentsDTO> filaments
)
{}
