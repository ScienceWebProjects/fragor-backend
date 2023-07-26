package com.filament.measurement.Authentication.DTO;

import lombok.Builder;

@Builder
public record AuthenticationTokenDTO(
        String token,
        String permission
) {}
