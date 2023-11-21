package com.filament.measurement.Filament.DTOMapper;

import com.filament.measurement.Filament.DTO.FilamentChartDTO;
import com.filament.measurement.Filament.Model.FilamentChart;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class FilamentChartDTOMapper implements Function<FilamentChart, FilamentChartDTO> {
    @Override
    public FilamentChartDTO apply(FilamentChart filamentChart) {
        return FilamentChartDTO.builder()
                .color(filamentChart.getColor())
                .material(filamentChart.getMaterial())
                .brand(filamentChart.getBrand())
                .quantity(filamentChart.getQuantity())
                .time(filamentChart.getTime())
                .build();
    }
}
