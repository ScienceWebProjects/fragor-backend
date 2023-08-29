package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Filament.Model.FilamentBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilamentBrandRepository extends JpaRepository<FilamentBrand,Long> {
    public List<FilamentBrand> findAllByCompanyId(Long id);
    @Query("""
            SELECT EXISTS(SELECT 1 FROM FilamentBrand fb WHERE fb.company = ?1 AND fb.brand = ?2)
            """)
    public Boolean brandExists(Company company,String brand);

    public Optional<FilamentBrand> findByCompanyAndId(Company company,Long id);
    public Optional<FilamentBrand> findByBrand(String brand);
}
