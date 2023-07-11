package com.filament.measurement.Form;

import com.filament.measurement.Validation.Anotation.UniqueCompanyToken;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyForm {
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Token can't be blank")
    @UniqueCompanyToken
    private String token;
}
