package com.filament.measurement.Filament.Request;

import com.filament.measurement.Validation.Anotation.UniqueFilamentMaterial;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilamentMaterialRequest {
    @NotBlank(message = "Filament's material can't be blank")
    @UniqueFilamentMaterial
    private String material;

    @NotBlank(message = "Filament's hotbed can't be blank")
    private String hotbed;

    @NotBlank(message = "Filament's hotend can't be blank")
    private String hotend;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "10.0")
    private double density;
}
