package com.filament.measurement.Authentication.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filament.measurement.Filament.Model.Filament;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
@Builder
//@ToString(exclude = "users")

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String token;

    @OneToMany(mappedBy = "company",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "company")
    private List<Filament> filaments;


}
