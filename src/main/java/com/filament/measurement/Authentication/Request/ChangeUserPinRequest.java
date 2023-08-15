package com.filament.measurement.Authentication.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserPinRequest {

    @NotBlank(message = "Password can't be blank")
    private String password;

    @NotBlank(message = "Pin can't be blank")
    private String pin;
}
