package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Filament.Model.Filament;
import com.filament.measurement.Filament.Model.FilamentBrand;
import com.filament.measurement.Filament.Model.FilamentColor;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FilamentRepository extends JpaRepository<Filament,Long> {
    List<Filament> findAllByCompany(Company company);
    Optional<Filament> findByIdAndCompany(Long id,Company company);
    @Query("""
            SELECT f FROM Filament f WHERE (:color IS NULL OR f.color =:color)
            AND(:material IS NULL OR f.material =:material)
            AND(:brand IS NULL OR f.brand =:brand)
            AND f.company =:company
            AND f.quantity > :quantity
            ORDER BY f.quantity DESC
            """)
    List<Filament> findByColorAndMaterialAndCompanyAndBrandAndQuantityLessThan(
            FilamentColor color,
            FilamentMaterial material,
            FilamentBrand brand,
            Company company,
            double quantity
            );
    Optional<Filament> findByUidAndCompany(Long uid,Company company);
//    @Query("""
//            SELECT DISTINCT fb.brand
//            FROM Filament f
//            JOIN f.brand fb.brand
//            WHERE f.company =:company
//            """)
//    List<Object[]> findAllFiltersByCompany(Company company);
}
