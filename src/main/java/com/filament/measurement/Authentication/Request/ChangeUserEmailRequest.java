package com.filament.measurement.Authentication.Request;

import com.filament.measurement.Validation.Anotation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserEmailRequest {
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Valid email")
    @UniqueEmail
    private String email;
}
