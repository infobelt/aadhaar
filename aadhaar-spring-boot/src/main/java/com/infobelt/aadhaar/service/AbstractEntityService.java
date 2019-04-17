package com.infobelt.aadhaar.service;

import com.infobelt.aadhaar.data.AbstractEntity;
import com.infobelt.aadhaar.query.QueryContext;
import com.infobelt.aadhaar.query.QueryContextRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * An abstract base for services that allows us to service most of the standard requests through a service
 * layer
 */
@Slf4j
public abstract class AbstractEntityService<T extends AbstractEntity> {

    @Autowired(required = false)
    EntityAuditor entityAuditor;

    @Autowired
    private JpaRepository<T, Long> jpaRepository;

    private QueryContextRepository<T> queryContextRepository;

    @Autowired
    protected EntityManager em;

    protected abstract Class<T> getEntityClass();

    public boolean isAuditLogged() {
        return true;
    }

    @PostConstruct
    private void initialize() {
        this.queryContextRepository = new QueryContextRepository<>(em, getEntityClass());
    }

    @Transactional
    public T save(T entity) {
        log.debug("Request to save : {}", entity);
        String userName = entityAuditor != null ? entityAuditor.getCurrentUsername() : "unknown";
        String oldValue = "";
        if (entity.getId() != null) {
            Optional<T> found = findOne(entity.getId());
            if (found.isPresent()) {
                T oldAttribute = found.get();
                oldValue = oldAttribute.toString();
            }
        }
        if (entity.getId() == null) {
            entity.setCreatedOn(ZonedDateTime.now());
            entity.setCreatedBy(userName);
        } else {
            entity.setUpdatedOn(ZonedDateTime.now());
            entity.setUpdatedBy(userName);
        }
        T result = jpaRepository.save(entity);

        if (isAuditLogged() && entityAuditor != null) {
            if (entity.getId() != null && oldValue.isEmpty()) {
                entityAuditor.audit(result, entity.getId(), AuditEvent.INSERT, result.toString(), "");

            } else if (entity.getId() != null && !result.toString().equals(oldValue)) {
                entityAuditor.audit(result, entity.getId(), AuditEvent.UPDATE, result.toString(), oldValue);
            }

        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        log.debug("Request to get all");
        return jpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(QueryContext queryContext) {
        log.debug("Request to get all using a query context {}", queryContext);
        return queryContextRepository.query(queryContext);
    }

    @Transactional(readOnly = true)
    public Optional<T> findOne(Long id) {
        log.debug("Request to get : {}", id);
        return jpaRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete : {}", id);
        jpaRepository.deleteById(id);
    }

    public String getEntityName() {
        return getEntityClass().getSimpleName();
    }

    public String getEntityPlural() {
        return getEntityClass().getSimpleName() + "s";
    }

    public long count() {
        return jpaRepository.count();
    }

    protected void flush() {
        jpaRepository.flush();
    }

    protected void deleteAll() {
        jpaRepository.deleteAll();
    }
}
