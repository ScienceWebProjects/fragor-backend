package com.filament.measurement.Device.DTOMapper;

import com.filament.measurement.Device.DTO.DeviceDTO;
import com.filament.measurement.Device.Model.Device;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DeviceDTOMapper implements Function<Device, DeviceDTO> {
    @Override
    public DeviceDTO apply(Device device) {
        return DeviceDTO.builder()
                .id(device.getId())
                .printer(device.getPrinter())
                .build();
    }
}
