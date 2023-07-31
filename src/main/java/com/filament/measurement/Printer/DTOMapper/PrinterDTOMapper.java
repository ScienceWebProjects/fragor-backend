package com.filament.measurement.Printer.DTOMapper;

import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.Model.Printer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PrinterDTOMapper implements Function<Printer, PrinterDTO> {
    @Override
    public PrinterDTO apply(Printer printer) {
        return PrinterDTO.builder()
                .id(printer.getId())
                .filaments(printer.getFilaments())
                .printerModel(printer.getPrinterModel())
                .device(printer.getDevice())
                .name(printer.getName())
                .workHours(printer.getWorkHours())
                .build();
    }
}
