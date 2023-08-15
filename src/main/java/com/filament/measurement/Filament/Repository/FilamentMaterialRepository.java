package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Filament.Model.FilamentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface FilamentMaterialRepository extends JpaRepository<FilamentMaterial,Long> {
    public Optional<FilamentMaterial> findByMaterial(String material);
}
