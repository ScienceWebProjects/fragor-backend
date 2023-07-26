package com.filament.measurement.Authentication.DTO;

public record UserPermissionDTO(
        String email,
        Boolean changer
) {
}
