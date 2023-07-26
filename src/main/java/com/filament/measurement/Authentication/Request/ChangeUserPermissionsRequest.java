package com.filament.measurement.Authentication.Request;


import lombok.Data;
@Data
public class ChangeUserPermissionsRequest {

    private Boolean changer;
    private String email;

}
