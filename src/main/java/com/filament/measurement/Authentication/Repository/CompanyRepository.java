package com.filament.measurement.Authentication.Repository;

import com.filament.measurement.Authentication.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByToken(String token);
    Optional<Company> findByName(String name);
    @Query("""
            SELECT EXISTS(SELECT 1 FROM Company c WHERE c.token =:token)
            """)
    Boolean tokenExists(String token);
}
