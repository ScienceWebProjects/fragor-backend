package com.filament.measurement.Filament.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilamentColorRequest {

    @NotBlank(message = "Filament's color can't be blank")
    private String color;
}
