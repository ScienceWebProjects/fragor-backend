package com.filament.measurement.Printer.DTOMapper;

import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.Model.Printer;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PrinterDTOMapper implements Function<Printer, PrinterDTO> {

    private final PrinterModelDTOMapper printerModelDTOMapper;
    private final PrinterFilamentsDTOMapper printerFilamentsDTOMapper;

    public PrinterDTOMapper(
            PrinterModelDTOMapper printerModelDTOMapper,
            PrinterFilamentsDTOMapper printerFilamentsDTOMapper
    ) {
        this.printerModelDTOMapper = printerModelDTOMapper;
        this.printerFilamentsDTOMapper = printerFilamentsDTOMapper;
    }

    @Override
    public PrinterDTO apply(Printer printer) {
        return PrinterDTO.builder()
                .id(printer.getId())
                .filaments(printer.getFilaments().stream().map(printerFilamentsDTOMapper).collect(Collectors.toList()))
                .model(printer.getPrinterModel().getModel())
                .name(printer.getName())
                .workHours(printer.getWorkHours())
                .build();
    }
}
