package com.infobelt.aadhaar.dto;

import lombok.Data;

import java.util.UUID;

/**
 * A DTO that is returned when we have an internal exception in the application
 */
@Data
public class ExceptionReport {

    private final String message;
    private final String description;
    private final String exceptionUuid = UUID.randomUUID().toString();

    public ExceptionReport(String message) {
        this(message, null);
    }

    public ExceptionReport(String message, String description) {
        this.message = message;
        this.description = description;
    }

}
