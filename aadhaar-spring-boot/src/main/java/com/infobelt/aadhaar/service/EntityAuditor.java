package com.infobelt.aadhaar.service;

/**
 * An interface that you would implement if you want to use the auditing of
 * the {@link AbstractEntityService}
 */
public interface EntityAuditor {

    /**
     * Return the current username that we will use in the auditing
     *
     * @return the current username
     */
    String getCurrentUsername();

    /**
     * Write the following audit information as needed
     *
     * @param id The ID of that object (or its association)
     * @param event The event that is being raised
     * @param newValue The old value
     * @param oldValue The new value
     */
    void audit(AuditEvent event, Object newValue, Object oldValue, Long id);

}
