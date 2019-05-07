package com.infobelt.aadhaar.service;

import com.infobelt.aadhaar.domain.AbstractEntity;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractAuditingEntityService<T extends AbstractEntity, A> extends AbstractEntityService<T> {

    @Autowired
    A customAuditor;

    protected boolean handleSaveAudit(T entity) {
        handleSaveAudit(customAuditor, entity);
        return true;
    }

    /**
     * Implement this method to handle a custom call to the auditor,
     * note that this will allow you to wire in subclasses of the EntityAuditor
     * which can support any additional auditing requirements
     * <p>
     * This will be called when a "save" is issued
     *
     * @param customAuditor
     * @param entity
     */
    protected abstract void handleSaveAudit(A customAuditor, T entity);

    protected boolean handleDeleteAudit(T entity) {
        handleDeleteAudit(customAuditor, entity);
        return true;
    }

    /**
     * Implement this method to handle a custom call to the auditor,
     * note that this will allow you to wire in subclasses of the EntityAuditor
     * which can support any additional auditing requirements
     * <p>
     * This will be called when a "delete" is issued
     *
     * @param customAuditor
     * @param entity
     */
    protected abstract void handleDeleteAudit(A customAuditor, T entity);
}
