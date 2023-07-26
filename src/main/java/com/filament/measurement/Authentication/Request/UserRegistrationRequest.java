package com.filament.measurement.Authentication.Request;

import com.filament.measurement.Validation.Anotation.CompanyTokenExist;
import com.filament.measurement.Validation.Anotation.PasswordMatch;
import com.filament.measurement.Validation.Anotation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
@PasswordMatch
public class UserRegistrationRequest {
    @NotBlank(message = "First name can't be blank")
    private String firstName;

    @NotBlank(message = "Last name can't be blank")
    private String lastName;

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Valid email")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Password can't be blank")
//    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
//            message = "The password must contain at least 8 characters, including 1 uppercase letter and 1 special character.")
    private String password;

    private String password2;
    @NotBlank(message = "Token can't be blank")
    @CompanyTokenExist
    private String token;

}
