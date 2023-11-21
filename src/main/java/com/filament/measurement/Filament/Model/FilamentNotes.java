package com.filament.measurement.Filament.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.filament.measurement.Printer.Model.Printer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilamentNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "filament_id"
    )

    @JsonBackReference
    private Filament filament;
}
