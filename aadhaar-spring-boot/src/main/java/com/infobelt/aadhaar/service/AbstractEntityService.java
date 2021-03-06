package com.infobelt.aadhaar.service;

import com.infobelt.aadhaar.domain.AbstractKeyed;
import com.infobelt.aadhaar.domain.SimpleAuditable;
import com.infobelt.aadhaar.query.QueryContext;
import com.infobelt.aadhaar.query.QueryContextRepository;
import com.infobelt.aadhaar.utils.SqlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.util.*;

//import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * An abstract base for services that allows us to service most of the standard requests through a service
 * layer
 */
@Transactional
@Service
@Slf4j
public abstract class AbstractEntityService<T extends AbstractKeyed> {

    @Autowired(required = false)
    @Getter
    EntityAuditor entityAuditor;

    @Autowired
    private JpaRepository<T, Long> jpaRepository;

//    @Autowired(required = false)
//    private ElasticsearchRepository<T, Long> searchRepository;

    private QueryContextRepository<T> queryContextRepository;

    @Autowired
    protected EntityManager em;

    protected abstract Class<T> getEntityClass();

    // Provide access to the log for subclasses
    public Logger log() {
        return this.log;
    }

//    public boolean isSearchIndexed() {
//        return searchRepository != null;
//    }

    public boolean isAuditLogged() {
        return true;
    }

    @PostConstruct
    private void initialize() {
        this.queryContextRepository = new QueryContextRepository<>(em, getEntityClass());
    }

    @Transactional
    public T save(T newInstance) {
        log.debug("Request to save : {}", newInstance);
        String userName = entityAuditor != null ? entityAuditor.getCurrentUsername() : "unknown";

        T oldInstance = newInstance.getId() != null ? findOne(newInstance.getId()).orElse(null) : null;

        if (newInstance instanceof SimpleAuditable) {
            SimpleAuditable simpleAuditable = (SimpleAuditable) newInstance;
            if (newInstance.getId() == null) {
                simpleAuditable.setCreatedOn(ZonedDateTime.now());
                simpleAuditable.setCreatedBy(userName);
            } else {
                simpleAuditable.setUpdatedOn(ZonedDateTime.now());
                simpleAuditable.setUpdatedBy(userName);
            }
        }

        // We will audit before we save otherwise we will have problems with JPA making
        // old and new instance the same

        boolean isNew = oldInstance == null;

        // If it is not new we need to audit before we save to ensure we have the old value
        if (isAuditLogged() && !isNew) {
            if (!this.handleSaveAudit(oldInstance, newInstance) && entityAuditor != null) {
                entityAuditor.audit(AuditEvent.UPDATE, newInstance, oldInstance, newInstance.getId());
            }
        }

        T result = jpaRepository.save(newInstance);
        jpaRepository.flush();

        // If it is new then we need to audit after the save so we have the ID
        if (isAuditLogged() && isNew) {
            if (!this.handleSaveAudit(null, newInstance) && entityAuditor != null) {
                entityAuditor.audit(AuditEvent.INSERT, result, null, result.getId());
            }
        }

//        if (searchRepository != null) {
//            searchRepository.save(result);
//        }


        return result;
    }

    protected boolean handleSaveAudit(T oldInstance, T newInstance) {
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

        jpaRepository.deleteById(entity.getId());

//        if (searchRepository != null) {
//            searchRepository.delete(entity);
//        }

        if (isAuditLogged() && !handleDeleteAudit(entity) && entityAuditor != null) {
            entityAuditor.audit(AuditEvent.DELETE, null, Hibernate.unproxy(entity), entity.getId());
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

//        if (searchRepository != null) {
//            searchRepository.deleteAll();
//        }
    }

//    @Transactional(readOnly = true)
//    public Page<T> search(String query, Pageable pageable) {
//        log().debug("Request to search for a page of {} with query {}", getEntityPlural(), query);
//        return searchRepository.search(queryStringQuery(query), pageable);
//    }


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

        if(queryContext.getFilters() != null){
            queryContext.getFilters().forEach((filter) -> {
                whereClauses.append(whereClauses.length() == 0 ? " WHERE " : " AND ");
                whereClauses.append(SqlUtil.buildWhereFromSimpleFilter(filter));
                Map.Entry<String, Object> mapping = SqlUtil.buildSimpleSelectorMapping(filter.getColumnName(), filter.getValue());
                selectorMapping.put(mapping.getKey(), mapping.getValue());
            });
        }

        if (queryContext.getQueryComplexFilter() != null) {
            queryContext.getQueryComplexFilter().getFilters().forEach((qcf) -> {
                whereClauses.append(whereClauses.length() == 0 ? " WHERE " : (qcf.getLogicGate() == null || qcf.getLogicGate().isEmpty()) ? " AND " : " " + qcf.getLogicGate() + " ");
                if (qcf.getField().equals("citationTextOld")) {
                    whereClauses.append("(");
                }
                whereClauses.append(SqlUtil.buildWhereFromComplexFilter(qcf));
                if (qcf.getField().equals("citationTextNew")) {
                    whereClauses.append(")");
                }
                Map.Entry<String, Object> mapping = SqlUtil.buildSelectorMapping(qcf);
                selectorMapping.put(mapping.getKey(), mapping.getValue());
            });
        }
        queryContext.getSorts().forEach((s) -> {
            if (orderByClauses.length() > 0) {
                orderByClauses.append(", ");
            }
            if (s.getColumnName().equalsIgnoreCase("fieldNumber") || s.getColumnName().equalsIgnoreCase("fieldNum") || s.getColumnName().equalsIgnoreCase("attribNum")) { //to sort according to decimal and alphabets
                orderByClauses.append("regexp_substr(regexp_replace(" + s.getColumnName() + ", '^\\.', '0.'), '^\\D*') ");
                orderByClauses.append(s.getDirection().toString());
                orderByClauses.append(" nulls first,");
                orderByClauses.append("to_number(regexp_substr(regexp_replace(" + s.getColumnName() + ", '^\\.', '0.'), '\\d+\\.?\\d*')) ");
            } else {
                orderByClauses.append(s.getColumnName() + " ");
            }
            orderByClauses.append(s.getDirection().toString());
        });

        if (orderByClauses.length() > 0) {
            orderByClauses.insert(0, " order by ");
        }

        queryAllSB.append(whereClauses);
        queryAllSB.append(orderByClauses);
        try {
            Query queryCount = SqlUtil.getNativeAllQuery(em, queryAllSB);
            queryCount = setMappedQueryParam(queryCount, selectorMapping);

            totalCount = queryCount.getResultList().size();

            int pageSize = queryContext.getPageSize();
            int page = queryContext.getPage();

            if (totalCount == pageSize) {
                queryContext.setPage(totalCount / pageSize);
            } else if (totalCount != pageSize && totalCount < pageSize * page) {
                queryContext.setPage(((int) Math.floor(totalCount / pageSize)) + 1);
            }
            pageable = PageRequest.of(queryContext.getPage() - 1, queryContext.getPageSize());

            Query queryAll = SqlUtil.getMappedQuery(em, queryAllSB, pageable, resultSetMappingName);
            queryAll = setMappedQueryParam(queryAll, selectorMapping);

            detailsList = (List<T>) queryAll.getResultList();

        } catch (Exception ex) {
            log.error("Exception caught while getting Grid Details. " + ex);
        }

        return new PageImpl<T>(detailsList, pageable, totalCount);
    }

    private Query setMappedQueryParam(Query finalQuery, HashMap<String, Object> selectorMapping) {
        for (Map.Entry<String, Object> selectorAssoc : selectorMapping.entrySet()) {
            if (!selectorAssoc.getValue().equals("")) {
                finalQuery.setParameter(selectorAssoc.getKey(), selectorAssoc.getValue());
            }
        }
        return finalQuery;
    }


    @Transactional(readOnly = true)
    public T fetchEntityGraph(long id, String graphName) {
        EntityGraph graph = this.em.getEntityGraph(graphName);
        Map hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", graph);
        return (T) this.em.find(getEntityClass(), id, hints);
    }

    /** ToDo:  
     * This method can be removed if we call getDetailsBySql and just set the page size
     * on the query context to a value we determine should be the most number of rows returned.
     *
     * Returning data with no sanity check on the amount of data being returned could result in
     * performance problems.
     * */
    @Transactional(readOnly = true)
    public <T> List<T> getExtractDetailsBySql(String baseQueryString, String resultSetMappingName) {
        List<T> detailsList = new ArrayList<T>();

        StringBuilder queryAllSB = new StringBuilder();
        queryAllSB.append(baseQueryString);

        try {
            Query queryAll = SqlUtil.getMappedQuery(em, queryAllSB, null, resultSetMappingName);
            detailsList = (List<T>) queryAll.getResultList();

        } catch (Exception ex) {
            log.error("Exception caught while getting Details for extract. " + ex);
        }

        return detailsList;
    }
}
