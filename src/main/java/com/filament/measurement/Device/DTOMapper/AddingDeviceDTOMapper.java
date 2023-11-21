package com.filament.measurement.Device.DTOMapper;


import com.filament.measurement.Device.DTO.AddingDeviceDTO;
import com.filament.measurement.Device.Model.AddingDevice;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class AddingDeviceDTOMapper implements Function<AddingDevice, AddingDeviceDTO> {

    @Override
    public AddingDeviceDTO apply(AddingDevice addingDevice) {
        return AddingDeviceDTO.builder()
                .id(addingDevice.getId())
                .name(addingDevice.getName())
                .build();
    }
}
