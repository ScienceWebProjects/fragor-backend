package com.filament.measurement.Printer.Repository;

import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PrinterNotesRepository extends JpaRepository<PrinterNotes, Long> {
    public Optional<PrinterNotes> findByPrinterAndId(Printer printer, Long id);
    public List<PrinterNotes> findByPrinter(Printer printer);
}
