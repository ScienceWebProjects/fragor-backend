package com.filament.measurement.Filament.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.filament.measurement.Authentication.Model.Company;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    private double diameter;
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    private FilamentColor color;

    @ManyToOne(fetch = FetchType.EAGER)
    private FilamentMaterial material;

    @ManyToOne(fetch = FetchType.EAGER)
    private FilamentBrand brand;

    @OneToMany(mappedBy = "filament",cascade = CascadeType.REFRESH)
    @JsonManagedReference
    private List<FilamentNotes> notes;
}
