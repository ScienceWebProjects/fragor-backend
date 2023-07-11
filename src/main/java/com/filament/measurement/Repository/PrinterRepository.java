package com.filament.measurement.Repository;

import com.filament.measurement.Model.Company;
import com.filament.measurement.Model.Printer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrinterRepository extends JpaRepository<Printer,Long> {
    @Query("""
            SELECT EXISTS(SELECT 1 FROM Printer p WHERE p.company = ?1 AND p.name = ?2)
            """)
    public Boolean nameExists(Company company,String name);

    public List<Printer> findAllByCompany(Company company);

    public Optional<Printer> findByCompanyAndId(Company company,Long id);
}
