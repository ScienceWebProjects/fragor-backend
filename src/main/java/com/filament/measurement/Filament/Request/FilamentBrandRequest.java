package com.filament.measurement.Filament.Request;

import com.filament.measurement.Validation.Anotation.UniqueFilamentBrand;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilamentBrandRequest {
    @NotBlank(message = "Filament's brand can't be blank")
    @UniqueFilamentBrand
    private String brand;
}
