package com.filament.measurement.Authentication.DTOMapper;

import com.filament.measurement.Authentication.DTO.ElectricityTariffDTO;
import com.filament.measurement.Authentication.Model.ElectricityTariff;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ElectricityTariffRequestDTOMapper implements Function<ElectricityTariff, ElectricityTariffDTO> {
    @Override
    public ElectricityTariffDTO apply(ElectricityTariff electricityTariff) {
        return ElectricityTariffDTO.builder()
                .id(electricityTariff.getId())
                .hourFrom(electricityTariff.getHourFrom())
                .hourTo(electricityTariff.getHourTo())
                .price(electricityTariff.getPrice())
                .workingDays(electricityTariff.isWorkingDays())
                .weekend(electricityTariff.isWeekend())
                .name(electricityTariff.getName())
                .build();
    }
}
