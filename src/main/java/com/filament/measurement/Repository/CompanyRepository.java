package com.filament.measurement.Repository;

import com.filament.measurement.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    public Optional<Company> findByToken(String token);
}
