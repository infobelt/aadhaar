package com.infobelt.aadhaar.service;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The types of AuditEvent that we raise
 */
public enum AuditEvent {

    OBJECT(""),
    /**
     * A new instance inserted into the database.
     */
    INSERT("added"),
    /**
     * A Skye component updated.
     */
    UPDATE("modified"),
    /**
     * A Skye component deleted.
     */
    DELETE("removed");

    private String value;

    AuditEvent(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
