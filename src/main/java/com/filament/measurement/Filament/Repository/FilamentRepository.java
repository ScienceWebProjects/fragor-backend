package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Filament.Model.Filament;
import com.filament.measurement.Filament.Model.FilamentColor;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FilamentRepository extends JpaRepository<Filament,Long> {
    List<Filament> findAllByCompany(Company company);

    Optional<Filament> findByIdAndCompany(Long id,Company company);
    @Query("""
            SELECT f FROM Filament f WHERE (:color IS NULL OR f.color =:color)
            AND(:material IS NULL OR f.material =:material)AND f.quantity > :quantity 
            AND f.company =:company ORDER BY f.quantity DESC
            """)
    List<Filament> findByColorAndMaterialAndQuantityLessThanAndCompany(
            FilamentColor color,
            FilamentMaterial material,
            double quantity,
            Company company
    );
    Optional<Filament> findByUid(Long uid);
}
