package com.infobelt.aadhaar.service;

import com.infobelt.aadhaar.domain.AbstractAssociatedEntity;
import com.infobelt.aadhaar.domain.AbstractKeyed;
import com.infobelt.aadhaar.domain.SimpleAuditable;
import com.infobelt.aadhaar.query.QueryContext;
import com.infobelt.aadhaar.query.QueryContextRepository;
import com.infobelt.aadhaar.utils.SqlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * An abstract base for services that allows us to service most of the standard requests through a service
 * layer
 */
@Service
@Slf4j
public abstract class AbstractEntityService<T extends AbstractKeyed> {

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

        if (entity instanceof SimpleAuditable) {
            SimpleAuditable simpleAuditable = (SimpleAuditable) entity;
            if (entity.getId() == null) {
                simpleAuditable.setCreatedOn(ZonedDateTime.now());
                simpleAuditable.setCreatedBy(userName);
            } else {
                simpleAuditable.setUpdatedOn(ZonedDateTime.now());
                simpleAuditable.setUpdatedBy(userName);
            }
        }
        T result = jpaRepository.save(entity);

        if (searchRepository != null) {
            searchRepository.save(entity);
        }

        if (isAuditLogged() && !handleSaveAudit(entity) && entityAuditor != null) {

            if (entity instanceof AbstractAssociatedEntity) {
                AbstractAssociatedEntity abstractAssociatedEntity = (AbstractAssociatedEntity) entity;
                if (abstractAssociatedEntity.getShContextRowKey() != null) {
                    entityAuditor.audit(AuditEvent.DISSOCIATE, result, null, abstractAssociatedEntity.getShContextRowKey());

                } else if (entity.getId() != null && !result.toString().equals(oldValue)) {
                    entityAuditor.audit(AuditEvent.ASSOCIATE, result, oldValue, abstractAssociatedEntity.getShContextRowKey());
                }
            } else {
                if (entity.getId() != null && oldValue.isEmpty()) {
                    entityAuditor.audit(AuditEvent.INSERT, result, null, entity.getId());

                } else if (entity.getId() != null && !result.toString().equals(oldValue)) {
                    entityAuditor.audit(AuditEvent.UPDATE, result, oldValue, entity.getId());
                }
            }

        }
        return result;
    }

    protected boolean handleSaveAudit(T entity) {
        return false;
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
        T instance = jpaRepository.getOne(id);
        delete(instance);
    }

    protected boolean handleDeleteAudit(T instance) {
        return false;
    }

    public void delete(T entity) {
        log.debug("Request to delete : {}", entity);
        jpaRepository.delete(entity);

        if (searchRepository != null) {
            searchRepository.delete(entity);
            handleDeleteAudit(entity);
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


    @Transactional(readOnly = true)
    public <T> Page<T> getDetailsBySql(QueryContext queryContext, String baseQueryString, String resultSetMappingName) {
        List<T> detailsList = new ArrayList<T>();
        int totalCount = 0;
        Pageable pageable = PageRequest.of(queryContext.getPage() - 1, queryContext.getPageSize());

        HashMap<String, Object> selectorMapping = new HashMap<>();
        StringBuilder queryAllSB = new StringBuilder();
        StringBuilder whereClauses = new StringBuilder();
        StringBuilder orderByClauses = new StringBuilder();

        queryAllSB.append(baseQueryString);

        if (queryContext.getQueryComplexFilter() != null) {
            queryContext.getQueryComplexFilter().getFilters().forEach((qcf) -> {
                whereClauses.append(whereClauses.length() == 0 ? " WHERE " : " AND ");
                whereClauses.append(SqlUtil.buildWhereFromComplexFilter(qcf));
                Map.Entry<String, Object> mapping = SqlUtil.buildSelectorMapping(qcf);
                selectorMapping.put(mapping.getKey(), mapping.getValue());
            });
        }
        queryContext.getSorts().forEach((s) -> {
            if (orderByClauses.length() > 0) {
                orderByClauses.append(", ");
            }
            orderByClauses.append(s.getColumnName() + " " + s.getDirection().toString());
        });

        if (orderByClauses.length() > 0) {
            orderByClauses.insert(0, " order by ");
        }

        queryAllSB.append(whereClauses);
        queryAllSB.append(orderByClauses);
        try {
            Query queryAll = SqlUtil.getMappedQuery(em, queryAllSB, pageable, resultSetMappingName);
            Query queryCount = SqlUtil.getNativeAllQuery(em, queryAllSB);

            for (Map.Entry<String, Object> selectorAssoc : selectorMapping.entrySet()) {
                queryAll.setParameter(selectorAssoc.getKey(), selectorAssoc.getValue());
                queryCount.setParameter(selectorAssoc.getKey(), selectorAssoc.getValue());
            }
            detailsList = (List<T>) queryAll.getResultList();
            totalCount = queryCount.getResultList().size();

        } catch (Exception ex) {
            log.error("Exception caught while getting Product Details. " + ex);
        }

        return new PageImpl<T>(detailsList, pageable, totalCount);
    }


    @Transactional(readOnly = true)
    public T fetchEntityGraph(long id, String graphName) {
        EntityGraph graph = this.em.getEntityGraph(graphName);
        Map hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", graph);
        return (T) this.em.find(getEntityClass(), id, hints);
    }
}
