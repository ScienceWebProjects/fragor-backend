package com.filament.measurement.Model;

import com.filament.measurement.Validation.Anotation.UniqueFilamentMaterial;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FilamentMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String material;
    private String hotbed;
    private String hotend;
    private double density;
    private double diameter;

}
