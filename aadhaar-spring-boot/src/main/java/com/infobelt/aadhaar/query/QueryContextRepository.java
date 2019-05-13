package com.infobelt.aadhaar.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation of a repository for Spring Data that is able to handle our
 * {@link QueryContext}
 */
public class QueryContextRepository<T> {

    protected Class<T> persistentClass;
    private EntityManager em;

    public QueryContextRepository(EntityManager em, Class clazz) {
        this.em = em;
        this.persistentClass = clazz;
    }

    private Object typeConvert(Path path, String value) {

        if (path.getJavaType().getName().equals("boolean") || path.getJavaType().equals(Boolean.class)) {
            return value == null ? null : Boolean.valueOf(value);
        } else if (path.getJavaType().getName().equals("int") || path.getJavaType().equals(Integer.class)) {
            try {
                return value == null ? null : Integer.valueOf(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (path.getJavaType().getName().equals("long") || path.getJavaType().equals(Long.class)) {
            try {
                return value == null ? null : Long.valueOf(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate buildComplexPredicate(QueryComplexFilter queryComplexFilter, CriteriaBuilder builder, Root root) {
        Object convertedValue = typeConvert(getReference(root, queryComplexFilter.getField()), queryComplexFilter.getValue());
        switch (queryComplexFilter.getOperator()) {
            case eq:
                if(queryComplexFilter.isIgnoreCase() && convertedValue instanceof String){
                    return builder.equal(builder.lower(getReference(root, queryComplexFilter.getField())), convertedValue.toString().toLowerCase());
                }
                return builder.equal(getReference(root, queryComplexFilter.getField()), convertedValue);
            case neq:
                if(queryComplexFilter.isIgnoreCase() && convertedValue instanceof String){
                    return builder.notEqual(builder.lower(getReference(root, queryComplexFilter.getField())), convertedValue.toString().toLowerCase());
                }
                return builder.notEqual(getReference(root, queryComplexFilter.getField()), convertedValue);
            case contains:
                if(queryComplexFilter.isIgnoreCase()){
                    return builder.like(builder.lower(getReference(root, queryComplexFilter.getField())), "%" + queryComplexFilter.getValue().toLowerCase() + "%");
                }
                return builder.like(getReference(root, queryComplexFilter.getField()), "%" + queryComplexFilter.getValue() + "%");
            case startswith:
                if(queryComplexFilter.isIgnoreCase()){
                    return builder.like(builder.lower(getReference(root, queryComplexFilter.getField())), queryComplexFilter.getValue().toLowerCase() + "%");
                }
                return builder.like(getReference(root, queryComplexFilter.getField()), queryComplexFilter.getValue() + "%");
            case endswith:
                if(queryComplexFilter.isIgnoreCase()){
                    return builder.like(builder.lower(getReference(root, queryComplexFilter.getField())), "%" + queryComplexFilter.getValue().toLowerCase());
                }
                return builder.like(getReference(root, queryComplexFilter.getField()), "%" + queryComplexFilter.getValue());
            case gte:
                return builder.greaterThanOrEqualTo(getReference(root, queryComplexFilter.getField()), queryComplexFilter.getValue());
            case gt:
                return builder.greaterThan(getReference(root, queryComplexFilter.getField()), queryComplexFilter.getValue());
            case lte:
                return builder.lessThanOrEqualTo(getReference(root, queryComplexFilter.getField()), queryComplexFilter.getValue());
            case lt:
                return builder.lessThan(getReference(root, queryComplexFilter.getField()), queryComplexFilter.getValue());
            case isnull:
                return builder.isNull(getReference(root, queryComplexFilter.getField()));
            case isempty:
                return builder.isNull(getReference(root, queryComplexFilter.getField()));
            case isnotnull:
                return builder.isNotNull(getReference(root, queryComplexFilter.getField()));
            default:
                throw new RuntimeException("Unsupported operator " + queryComplexFilter.getOperator());
        }
    }

    private Path getReference(Root root, String field) {
        if (!field.contains(".")) {
            return root.get(field);
        } else {
            return getPath(root.get(field.substring(0, field.indexOf('.'))), field.substring(field.indexOf('.') + 1));
        }

    }

    private Path getPath(Path path, String substring) {
        if (!substring.contains(".")) {
            return path.get(substring);
        } else {
            return getPath(path.get(substring.substring(0, substring.indexOf('.'))), substring.substring(substring.indexOf('.') + 1));
        }
    }

    @SuppressWarnings("unchecked")
    public Page<T> query(QueryContext queryContext) {

        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<T> query = builder.createQuery(persistentClass);
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

        Class clazz = this.persistentClass;
        Root root = query.from(clazz);
        Root countRoot = countQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        queryContext.getFilters().forEach(filter -> {
            predicates.add(builder.equal(getReference(root, filter.getColumnName()), filter.getValue()));
            countPredicates.add(builder.equal(getReference(countRoot, filter.getColumnName()), filter.getValue()));
        });

        if (queryContext.getQueryComplexFilter() != null) {
            queryContext.getQueryComplexFilter().getFilters().forEach(qcf -> predicates.add(buildComplexPredicate(qcf, builder, root)));
            queryContext.getQueryComplexFilter().getFilters().forEach(qcf -> countPredicates.add(buildComplexPredicate(qcf, builder, countRoot)));
        }

        List<Order> orderBy = new ArrayList<>();
        queryContext.getSorts().forEach(sort -> {
            if (sort.getDirection().equals(SortDirection.asc)) {
                orderBy.add(builder.asc(getReference(root, sort.getColumnName())));
            } else {
                orderBy.add(builder.desc(getReference(root, sort.getColumnName())));
            }
        });

        if (!predicates.isEmpty()) {
            Predicate filters = builder.and(predicates.toArray(new Predicate[predicates.size()]));
            Predicate countFilters = builder.and(countPredicates.toArray(new Predicate[countPredicates.size()]));

            query.where(filters);
            countQuery.where(countFilters);
        }

        if (!orderBy.isEmpty()) {
            query.orderBy(orderBy);
        }

        query.select(root);


        countQuery.select(builder.count(countRoot));
        Long count = em.createQuery(countQuery).getSingleResult();

        TypedQuery<T> finalQuery = em.createQuery(query);

        Pageable pageable = PageRequest.of(queryContext.getPage() - 1, queryContext.getPageSize());
        return new PageImpl(finalQuery
            .setFirstResult((queryContext.getPage() - 1) * queryContext.getPageSize()) // offset
            .setMaxResults(queryContext.getPageSize()) // limit
            .getResultList(), pageable, count);
    }
}

