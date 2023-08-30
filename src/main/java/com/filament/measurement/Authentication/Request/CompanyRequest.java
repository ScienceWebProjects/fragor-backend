package com.filament.measurement.Authentication.Request;

import com.filament.measurement.Validation.Anotation.UniqueCompanyName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyRequest {
    @NotBlank(message = "Name can't be blank")
    @UniqueCompanyName
    private String name;
}
