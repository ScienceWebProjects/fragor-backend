package com.filament.measurement.Authentication.Request;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String email;
    String password;
    String pin;
}
