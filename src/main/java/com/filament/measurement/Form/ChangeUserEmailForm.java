package com.filament.measurement.Form;

import com.filament.measurement.Validation.Anotation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserEmailForm {
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Valid email")
    @UniqueEmail
    private String email;
}
