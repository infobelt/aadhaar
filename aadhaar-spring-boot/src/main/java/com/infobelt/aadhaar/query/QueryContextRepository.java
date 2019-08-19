package com.infobelt.aadhaar.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
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


    private Object typeConvert(Class clazz, String value) {

        if (clazz.getName().equals("boolean") || clazz.equals(Boolean.class)) {
            return value == null ? null : Boolean.valueOf(value);
        } else if (clazz.getName().equals("int") || clazz.equals(Integer.class)) {
            try {
                return value == null ? null : Integer.valueOf(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (clazz.getName().equals("long") || clazz.equals(Long.class)) {
            try {
                return value == null ? null : Long.valueOf(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (clazz.equals(ZonedDateTime.class)){
            try{
                //First, decode date - date is encoded to prevent the lost of '+'
                return value == null ? null : ZonedDateTime.parse(value.replace("%2B", "+"));
            } catch (DateTimeParseException e){
                return null;
            }
        } else if (clazz.equals(LocalDate.class)){
            try{
                return value == null ? null : LocalDate.parse(value.split("T")[0]);
            } catch (DateTimeParseException e){
                return null;
            }
        } else {
            return value;
        }
    }



    @SuppressWarnings("unchecked")
    private Predicate buildComplexPredicate(QueryComplexFilter queryComplexFilter, CriteriaBuilder builder, Root root) {
        Path path = getReference(root, queryComplexFilter.getField());
        Class clazz = path.getJavaType();
        Object convertedValue = typeConvert(clazz, queryComplexFilter.getValue());
        switch (queryComplexFilter.getOperator()) {
            case eq:
                if(queryComplexFilter.isIgnoreCase() && convertedValue instanceof String){
                    return builder.equal(builder.lower(path), convertedValue.toString().toLowerCase());
                }
                return builder.equal(path, convertedValue);
            case neq:
                if(queryComplexFilter.isIgnoreCase() && convertedValue instanceof String){
                    return builder.notEqual(builder.lower(path), convertedValue.toString().toLowerCase());
                }
                return builder.notEqual(path, convertedValue);
            case contains:
                if(queryComplexFilter.isIgnoreCase()){
                    return builder.like(builder.lower(path), "%" + queryComplexFilter.getValue().toLowerCase() + "%");
                }
                return builder.like(path, "%" + queryComplexFilter.getValue() + "%");
            case startswith:
                if(queryComplexFilter.isIgnoreCase()){
                    return builder.like(builder.lower(path), queryComplexFilter.getValue().toLowerCase() + "%");
                }
                return builder.like(path, queryComplexFilter.getValue() + "%");
            case endswith:
                if(queryComplexFilter.isIgnoreCase()){
                    return builder.like(builder.lower(path), "%" + queryComplexFilter.getValue().toLowerCase());
                }
                return builder.like(path, "%" + queryComplexFilter.getValue());
            case gte:
            case gte_date:
                return builder.greaterThanOrEqualTo(path, (Comparable) convertedValue);
            case gt:
                return builder.greaterThan(path, (Comparable) convertedValue);
            case lte:
            case lte_date:
                return builder.lessThanOrEqualTo(path, (Comparable) convertedValue);
            case lt:
                return builder.lessThan(path, (Comparable) convertedValue);
            case isnull:
                return builder.isNull(path);
            case isempty:
                return builder.isNull(path);
            case isnotnull:
                return builder.isNotNull(path);
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

