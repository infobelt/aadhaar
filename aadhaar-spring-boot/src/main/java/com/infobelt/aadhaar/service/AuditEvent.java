package com.infobelt.aadhaar.service;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The types of AuditEvent that we raise
 */
public enum AuditEvent {

    OBJECT(""),
    /**
     * A new instance of a Skye component was inserted into the database.
     */
    INSERT("added"),
    /**
     * A Skye component was updated.
     */
    UPDATE("modified"),
    /**
     * A Skye component was deleted.
     */
    DELETE("removed"),
    /**
     * A user performed a search.
     */
    SEARCH("searched"),
    /**
     * A entity is associated to another
     */
    ASSOCIATE("associated"),
    /**
     * A entity is dissociated to another
     */
    DISSOCIATE("dissociated"),
    /**
     * A task is canceled by the user
     */
    CANCELED("cancelled"),
    /**
     * A task is canceled by the user
     */
    REFRESH("refreshed");

    private String value;

    AuditEvent(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
