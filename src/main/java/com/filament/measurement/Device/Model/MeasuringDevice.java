package com.filament.measurement.Device.Model;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Permission.Role;
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
@Data
@Builder
public class MeasuringDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;
    private InetAddress ip;
    private int port;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(cascade = CascadeType.ALL)
    private Printer printer;

}
