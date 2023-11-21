package com.filament.measurement.Filament.Request;

import com.filament.measurement.Validation.Anotation.UniqueFilamentColorName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilamentColorRequest {
    @NotBlank(message = "Filament's color can't be blank")
    @UniqueFilamentColorName
    private String color;
}
