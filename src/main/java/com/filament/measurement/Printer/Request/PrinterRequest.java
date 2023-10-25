package com.filament.measurement.Printer.Request;

import com.filament.measurement.Validation.Anotation.UniquePrinterName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrinterRequest {
    @NotNull(message = "Printer's name can't be null.")
    @UniquePrinterName
    private String name;
    @NotNull(message = "Printer's model can't be null.")
    private String model;
}
