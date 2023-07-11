package com.filament.measurement.Form;

import lombok.Data;

import java.net.InetAddress;

@Data
public class FilamentSubtraction {
    private long uid;
    private InetAddress ip;
    private int hours;
    private int quantity;
}
