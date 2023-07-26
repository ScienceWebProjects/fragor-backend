package com.filament.measurement.Printer.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Printer.Model.PrinterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrinterModelRepository extends JpaRepository<PrinterModel,Long> {
    @Query("""
            SELECT EXISTS( SELECT 1 FROM PrinterModel p WHERE p.company = ?1 AND p.model = ?2)
            """)
    public Boolean modelExists(Company company,String model);
    public List<PrinterModel> findAllByCompany(Company company);
    public Optional<PrinterModel> findByCompanyAndId(Company company,Long id);
    public Optional<PrinterModel> findByCompanyAndModel(Company company,String model);
}
