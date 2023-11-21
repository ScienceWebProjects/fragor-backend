package com.filament.measurement.Printer.DTOMapper;

import com.filament.measurement.Printer.DTO.PrinterFilamentsDTO;
import com.filament.measurement.Printer.Model.PrinterFilaments;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class PrinterFilamentsDTOMapper implements Function<PrinterFilaments, PrinterFilamentsDTO> {
    @Override
    public PrinterFilamentsDTO apply(PrinterFilaments printerFilaments) {
        return PrinterFilamentsDTO.builder()
                .amount(printerFilaments.getAmount())
                .filamentMaterial(printerFilaments.getFilamentMaterial().getMaterial())
                .price(printerFilaments.getPrice())
                .build();
    }
}
