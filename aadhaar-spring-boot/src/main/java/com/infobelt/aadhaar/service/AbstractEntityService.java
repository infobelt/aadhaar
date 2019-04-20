package com.infobelt.aadhaar.service;

import com.infobelt.aadhaar.domain.AbstractAssociatedEntity;
import com.infobelt.aadhaar.domain.AbstractEntity;
import com.infobelt.aadhaar.query.QueryContext;
import com.infobelt.aadhaar.query.QueryContextRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * An abstract base for services that allows us to service most of the standard requests through a service
 * layer
 */
@Service
@Slf4j
public abstract class AbstractEntityService<T extends AbstractEntity> {

    @Autowired(required = false)
    @Getter
    EntityAuditor entityAuditor;

    @Autowired
    private JpaRepository<T, Long> jpaRepository;

    @Autowired(required = false)
    private ElasticsearchRepository<T, Long> searchRepository;

    private QueryContextRepository<T> queryContextRepository;

    @Autowired
    protected EntityManager em;

    protected abstract Class<T> getEntityClass();

    // Provide access to the log for subclasses
    public Logger log() {
        return this.log;
    }

    public boolean isSearchIndexed() {
        return searchRepository != null;
    }

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

        if (searchRepository != null) {
            searchRepository.save(entity);
        }

        if (isAuditLogged() && entityAuditor != null) {

            if (entity instanceof AbstractAssociatedEntity) {
                AbstractAssociatedEntity abstractAssociatedEntity = (AbstractAssociatedEntity) entity;
                if (abstractAssociatedEntity.getShContextRowKey() != null) {
                    entityAuditor.audit(result, abstractAssociatedEntity.getShContextRowKey(), AuditEvent.DISSOCIATE, result.toString(), "");

                } else if (entity.getId() != null && !result.toString().equals(oldValue)) {
                    entityAuditor.audit(result, abstractAssociatedEntity.getShContextRowKey(), AuditEvent.ASSOCIATE, result.toString(), oldValue);
                }
            } else {
                if (entity.getId() != null && oldValue.isEmpty()) {
                    entityAuditor.audit(result, entity.getId(), AuditEvent.INSERT, result.toString(), "");

                } else if (entity.getId() != null && !result.toString().equals(oldValue)) {
                    entityAuditor.audit(result, entity.getId(), AuditEvent.UPDATE, result.toString(), oldValue);
                }
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

        if (searchRepository != null) {
            searchRepository.deleteById(id);
        }
    }

    public void delete(T entity) {
        log.debug("Request to delete : {}", entity);
        jpaRepository.delete(entity);

        if (searchRepository != null) {
            searchRepository.delete(entity);
        }
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

    public void flush() {
        jpaRepository.flush();
    }

    public void deleteAll() {
        jpaRepository.deleteAll();

        if (searchRepository != null) {
            searchRepository.deleteAll();
        }
    }

    @Transactional(readOnly = true)
    public Page<T> search(String query, Pageable pageable) {
        log().debug("Request to search for a page of {} with query {}", getEntityPlural(), query);
        return searchRepository.search(queryStringQuery(query), pageable);
    }
}
