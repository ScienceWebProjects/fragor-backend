package com.filament.measurement.Filament.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilamentBrandRequest {
    @NotBlank(message = "Filament's brand can't be blank")
    private String brand;
}
