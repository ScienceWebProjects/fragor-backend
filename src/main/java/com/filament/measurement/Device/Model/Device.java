package com.filament.measurement.Device.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Printer.Model.Printer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;
    @OneToOne(cascade = CascadeType.ALL)
    private Printer printer;
    private InetAddress ip;
    private int port;
}
