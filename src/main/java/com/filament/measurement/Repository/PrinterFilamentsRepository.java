package com.filament.measurement.Repository;

import com.filament.measurement.Model.FilamentMaterial;
import com.filament.measurement.Model.Printer;
import com.filament.measurement.Model.PrinterFilaments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrinterFilamentsRepository extends JpaRepository<PrinterFilaments,Long> {

    Optional<PrinterFilaments> findByPrinterAndFilamentMaterial(Printer printer, FilamentMaterial filamentMaterial);
}
