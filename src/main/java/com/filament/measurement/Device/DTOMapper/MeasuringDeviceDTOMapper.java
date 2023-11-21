package com.filament.measurement.Device.DTOMapper;

import com.filament.measurement.Device.DTO.MeasuringDeviceDTO;
import com.filament.measurement.Device.Model.MeasuringDevice;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class MeasuringDeviceDTOMapper implements Function<MeasuringDevice, MeasuringDeviceDTO> {
    @Override
    public MeasuringDeviceDTO apply(MeasuringDevice measuringDevice) {
        return MeasuringDeviceDTO.builder()
                .id(measuringDevice.getId())
                .printer(measuringDevice.getPrinter())
                .build();
    }
}
