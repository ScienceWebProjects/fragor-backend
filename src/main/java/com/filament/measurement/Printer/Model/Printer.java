package com.filament.measurement.Printer.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Device.Model.Device;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Printer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Company company;

    private String name;

    private double workHours;

    @ManyToOne(fetch = FetchType.EAGER)
    private PrinterModel  printerModel;

    @OneToMany(mappedBy = "printer",cascade = CascadeType.REFRESH)
    private List<PrinterFilaments> filaments;

    @OneToOne()
    private Device device;
}
