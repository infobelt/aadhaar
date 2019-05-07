package com.infobelt.aadhaar.example.services;

import com.infobelt.aadhaar.example.domain.Thingy;
import com.infobelt.aadhaar.service.AbstractAuditingEntityService;
import org.springframework.stereotype.Service;

@Service
public class ThingyService extends AbstractAuditingEntityService<Thingy, CustomAuditor> {

    @Override
    protected Class<Thingy> getEntityClass() {
        return Thingy.class;
    }

    @Override
    protected void handleSaveAudit(CustomAuditor customAuditor, Thingy entity) {
        customAuditor.auditLogMe(entity);
    }

    @Override
    protected void handleDeleteAudit(CustomAuditor customAuditor, Thingy entity) {
        customAuditor.auditLogMe(entity);
    }
}
