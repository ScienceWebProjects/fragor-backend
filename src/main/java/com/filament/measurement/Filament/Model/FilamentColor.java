package com.filament.measurement.Filament.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filament.measurement.Authentication.Model.Company;
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
public class FilamentColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

}