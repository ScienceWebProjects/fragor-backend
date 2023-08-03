package com.filament.measurement.Printer.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrinterRequest {
    @NotNull(message = "Printer's name can't be null.")
    private String name;
    @NotNull(message = "Printer's model can't be null.")
    private String model;
}
