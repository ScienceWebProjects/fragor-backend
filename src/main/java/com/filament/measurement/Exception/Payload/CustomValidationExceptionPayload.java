package com.filament.measurement.Exception.Payload;

public class CustomValidationExceptionPayload {
    private final String message;

    public CustomValidationExceptionPayload(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
