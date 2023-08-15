package com.filament.measurement.Authentication.DTO;

import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String name,
        String surname,
        String permission,
        String email
) {
}
