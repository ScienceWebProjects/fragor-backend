package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Filament.Model.FilamentColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FilamentColorRepository extends JpaRepository<FilamentColor,Long> {
    public Optional<FilamentColor> findByColor(String color);
    public Optional<FilamentColor> findByColorAndCompany(String color,Company company);
    @Query("""
            SELECT EXISTS( SELECT 1 FROM FilamentColor f WHERE f.company = ?1 AND color = ?2)
            """)
    public Boolean colorExists(Company company, String color);

    public List<FilamentColor> findAllByCompanyId(Long id);
    public Optional<FilamentColor> findByIdAndCompany(Long id,Company company);
}
