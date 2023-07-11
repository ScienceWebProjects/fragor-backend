package com.filament.measurement.Exception;

public class NotFound404Exception extends RuntimeException {
    public NotFound404Exception(String message) {
        super(message);
    }

    public NotFound404Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
