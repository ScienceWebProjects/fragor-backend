package com.filament.measurement.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Company company;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Printer printer;
    private InetAddress ip;
    private int port;
}
