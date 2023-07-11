package com.filament.measurement.Form;

import com.filament.measurement.Validation.Anotation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeUserPasswordForm {
    @NotBlank(message = "Old password can't be blank")
    private String oldPassword;

    @NotBlank(message = "Password can't be blank")
//    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
//            message = "The password must contain at least 8 characters, including 1 uppercase letter and 1 special character.")
    private String password;

    @NotBlank(message = "Confirm password can't be blank")
    private String password2;
}
