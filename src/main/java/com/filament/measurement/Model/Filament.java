package com.filament.measurement.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Filament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double quantity;

    private Long uid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    private FilamentColor color;

    @ManyToOne(fetch = FetchType.EAGER)
    private  FilamentMaterial material;
}
