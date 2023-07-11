package com.filament.measurement.Form;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilamentForm {
    @DecimalMin(value = "100.0")
    @DecimalMax(value = "5000.0")
    private double quantity = 1000;

    private Long uid;

    @NotNull
    private String color;

    @NotNull
    private String material;
}
