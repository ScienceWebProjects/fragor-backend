package com.filament.measurement.Device.DTO;

import lombok.Builder;

@Builder
public record AddingDeviceDTO(
    Long id,
    String name
) {}
