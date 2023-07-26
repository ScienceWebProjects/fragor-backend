package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Filament.Model.FilamentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilamentMaterialRepository extends JpaRepository<FilamentMaterial,Long> {
    public Optional<FilamentMaterial> findByMaterial(String material);
}
