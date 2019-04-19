package com.infobelt.aadhaar.service;

/**
 * An interface that you would implement if you want to use the auditing of
 * the {@link AbstractEntityService}
 */
public interface EntityAuditor {

    /**
     * Return the current username that we will use in the auditing
     * @return
     */
    String getCurrentUsername();

    /**
     * Write the following audit information as needed
     *
     * @param object The domain object that is impacts
     * @param id The ID of that object (or its association)
     * @param event The event that is being raised
     * @param newValue The old value (descriptive)
     * @param oldValue The new value (descriptive)
     */
    void audit(Object object, Long id, AuditEvent event, String newValue, String oldValue);

}
