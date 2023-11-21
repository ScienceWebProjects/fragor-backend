package com.filament.measurement.Device.Request;

import com.filament.measurement.Validation.Anotation.UniqueDeviceName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddingDeviceRequest {
    @NotNull
    @UniqueDeviceName
    String name;
}
