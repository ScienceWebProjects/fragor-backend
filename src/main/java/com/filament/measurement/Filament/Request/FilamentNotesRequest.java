package com.filament.measurement.Filament.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilamentNotesRequest {
    private Long noteID;
    @NotNull
    private String note;
}
