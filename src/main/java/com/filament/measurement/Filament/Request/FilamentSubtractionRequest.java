package com.filament.measurement.Filament.Request;

import lombok.Data;

import java.net.InetAddress;

@Data
public class FilamentSubtractionRequest {
    private long uid;
    private InetAddress ip;
    private double hours;
    private int quantity;
    private String company;
}
