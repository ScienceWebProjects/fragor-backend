package com.filament.measurement.Printer.DTOMapper;

import com.filament.measurement.Printer.DTO.PrinterModelDTO;
import com.filament.measurement.Printer.Model.PrinterModel;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PrinterModelDTOMapper implements Function<PrinterModel, PrinterModelDTO> {
    @Override
    public PrinterModelDTO apply(PrinterModel printerModel) {
        return PrinterModelDTO.builder()
                .id(printerModel.getId())
                .model(printerModel.getModel())
                .build();
    }
}
