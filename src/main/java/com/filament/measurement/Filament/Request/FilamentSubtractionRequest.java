package com.filament.measurement.Filament.Request;

import lombok.Data;

import java.net.InetAddress;

@Data
public class FilamentSubtractionRequest {
    private long uid;
    private InetAddress ip;
    private int hours;
    private int quantity;
}
