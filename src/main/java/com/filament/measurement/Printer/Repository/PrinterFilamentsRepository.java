package com.filament.measurement.Printer.Repository;

import com.filament.measurement.Filament.Model.FilamentMaterial;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterFilaments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PrinterFilamentsRepository extends JpaRepository<PrinterFilaments,Long> {

    Optional<PrinterFilaments> findByPrinterAndFilamentMaterial(Printer printer, FilamentMaterial filamentMaterial);
}
