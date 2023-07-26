package com.filament.measurement.Printer.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PrinterFilaments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Printer printer;
    @ManyToOne(fetch = FetchType.EAGER)
    private FilamentMaterial filamentMaterial;
    private double amount;
}
