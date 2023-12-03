package com.filament.measurement.Authentication.Request;

import com.filament.measurement.Validation.Anotation.UniqueElectricityTariffName;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NonNull;

@Data
public class ElectricityTariffRequest {

    private Long id;
    @NonNull
//    @UniqueElectricityTariffName
    private String name;
    @NonNull
    @Min(0)
    @Max(24)
    private Integer hourFrom;
    @NonNull
    @Min(0)
    @Max(24)
    private Integer hourTo;
    private boolean workingDays;
    private boolean weekend;
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "10.1")
    private double price;

}
