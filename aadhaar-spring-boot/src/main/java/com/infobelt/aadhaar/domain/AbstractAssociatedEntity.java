package com.infobelt.aadhaar.domain;

/**
 * A type of abstract entity that is associatable/disassociatable with another, this impacts the
 * audit log calls
 */
public abstract class AbstractAssociatedEntity extends AbstractEntity {

    public abstract String getShContextTableAffected();

    public abstract Long getShContextRowKey();

}
