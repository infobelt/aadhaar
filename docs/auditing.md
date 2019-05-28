Auditing
====

The basic implementation of our AbstractEntity infrastructure
comes with the ability to integrate an auditor.

Typically this is done when you create a service that implements the *EntityAuditor* interface.

```java
@Service
public class MyAuditor implements EntityAuditor {
    
    /// Implementation
    
}
```

This will automatically be wired in, and used, by all implementations of the AbstractEntityService.

However, this interface is quite limited in terms of your ability to write complex audit logs.

If you wish to have a more complex configuration in place, where you want more control over the logging then you should consider
using the AbstractAuditingEntityService.

You would typically extend this the same you would the AbstractEntityService, however the difference
is you would now implement the audit handling on save and delete yourself.

```java
@Service
public class ThingyService extends AbstractAuditingEntityService<Thingy, CustomAuditor> {

    @Override
    protected Class<Thingy> getEntityClass() {
        return Thingy.class;
    }

    @Override
    protected void handleSaveAudit(CustomAuditor customAuditor, Thingy oldInstance, Thingy newInstance) {
        customAuditor.auditLogMe(newInstance);
    }

    @Override
    protected void handleDeleteAudit(CustomAuditor customAuditor, Thingy entity) {
        customAuditor.auditLogMe(entity);
    }
}
```

(You can see an example in the example project)

Note that you would declare as the second Generic in the extends to determine
the class of the auditor you wish to use, this will be automatically wired in and 
passed to you.

There is no need to implement a specific interface and you can have any methods you want on your
auditor, in order to provide flexibility.
