package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Filament.Model.Filament;
import com.filament.measurement.Filament.Model.FilamentNotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilamentNoteRepository extends JpaRepository<FilamentNotes,Long> {

    Optional<FilamentNotes> findByFilamentAndId(Filament filament, Long id);
}
