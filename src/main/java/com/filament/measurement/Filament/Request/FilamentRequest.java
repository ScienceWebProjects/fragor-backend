package com.filament.measurement.Filament.Request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilamentRequest {
    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5000.0")
    private double quantity = 1000;

    @NotNull
    private String color;

    @NotNull
    private String material;

    @NotNull
    private String brand;

    private double price;

    @DecimalMin(value = "1.0")
    @DecimalMax(value = "5.0")
    private double diameter;
    @NotNull
    private long device;
}
