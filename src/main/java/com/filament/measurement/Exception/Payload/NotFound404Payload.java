package com.filament.measurement.Exception.Payload;

public class NotFound404Payload {
    private final String message;

    public NotFound404Payload(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
