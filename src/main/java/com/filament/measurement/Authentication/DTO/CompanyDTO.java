package com.filament.measurement.Authentication.DTO;

import lombok.Builder;

@Builder
public record CompanyDTO(
        Long id,
        String company,
        String token
) {
}
