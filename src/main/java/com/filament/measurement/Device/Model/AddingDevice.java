package com.filament.measurement.Device.Model;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Permission.Role;
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
public class AddingDevice{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;
    private InetAddress ip;
    private int port;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String name;
}
