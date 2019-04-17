package com.infobelt.aadhaar.service;

/**
 * An interface that you would implement if you want to use the auditing of
 * the {@link AbstractEntityService}
 */
public interface EntityAuditor {

    String getCurrentUsername();

    void audit(Object object, Long id, AuditEvent event, String newValue, String oldValue);
}
