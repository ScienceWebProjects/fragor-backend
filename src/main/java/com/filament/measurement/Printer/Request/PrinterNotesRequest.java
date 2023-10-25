package com.filament.measurement.Printer.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrinterNotesRequest {
    private Long noteID;
    @NotNull
    private String note;
}
