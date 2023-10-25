package com.filament.measurement.Device.DTO;

import com.filament.measurement.Printer.Model.Printer;
import lombok.Builder;

@Builder
public record MeasuringDeviceDTO(
    Long id,
    Printer printer
) {
}
