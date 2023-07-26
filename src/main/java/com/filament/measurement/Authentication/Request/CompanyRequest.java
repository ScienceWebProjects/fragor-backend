package com.filament.measurement.Authentication.Request;

import com.filament.measurement.Validation.Anotation.UniqueCompanyToken;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyRequest {
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Token can't be blank")
    @UniqueCompanyToken
    private String token;
}
