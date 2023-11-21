package com.filament.measurement.Printer.Request;

import com.filament.measurement.Validation.Anotation.UniquePrinterModel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrinterModelRequest {
    @NotNull(message = "Printer's model can't be null.")
    @UniquePrinterModel
    private String model;
}
