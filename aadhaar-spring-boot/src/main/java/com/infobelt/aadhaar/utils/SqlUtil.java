package com.infobelt.aadhaar.utils;

import com.infobelt.aadhaar.query.QueryComplexFilter;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import java.util.AbstractMap;
import java.util.Map;

public class SqlUtil {

    public static Query getNativeAllQuery(EntityManager entityManager, StringBuilder query) {
        return
                entityManager
                        .createNativeQuery(query.toString())
                        .setFlushMode(FlushModeType.COMMIT);
    }

    public static Query getMappedQuery(EntityManager entityManager, StringBuilder query, Pageable pageable, String resultSetName){

        Query executeQuery = entityManager
                .createNativeQuery(query.toString(), resultSetName)
                .setFlushMode(FlushModeType.COMMIT)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());

        return executeQuery;
    }

    public static String buildWhereFromComplexFilter(QueryComplexFilter queryComplexFilter) {
        String field = queryComplexFilter.getField();
        switch(queryComplexFilter.getOperator()) {
            case eq:
                return " LOWER(" + field + ") like NVL2(:" + field + "Param, LOWER(:" + field + "Param), LOWER(" + field + "))";
            case contains:
                return " LOWER(" + field + ") like NVL2(:" + field + "Param, LOWER(CONCAT(CONCAT('%',:" + field + "Param), '%')), LOWER(" + field + "))";
            case startswith:
                return " LOWER(" + field + ") like NVL2(:" + field + "Param, LOWER(CONCAT(:" + field + "Param, '%')), LOWER(" + field + "))";
            case endswith:
                return " LOWER(" + field + ") like NVL2(:" + field + "Param, LOWER(CONCAT('%', :" + field + "Param)), LOWER(" + field + "))";
            case gte:
                return " " + field + " >= NVL2(:" + field + "Param, :" + field + "Param, " + field + ")";
            case gt:
                return " " + field + " > NVL2(:" + field + "Param, :" + field + "Param, " + field + ")";
            case lte:
                return " " + field + " <= NVL2(:" + field + "Param, :" + field + "Param, " + field + ")";
            case lt:
                return " " + field + " < NVL2(:" + field + "Param, :" + field + "Param, " + field + ")";
            case gte_date:
                return " " + field + " >= NVL2(:" + field + "ParamDateGTE, TO_DATE(:" + field + "ParamDateGTE,'DD/MM/YYYY'), " + field + ")";
            case lte_date:
                return " " + field + " <= NVL2(:" + field + "ParamDateLTE, TO_DATE(:" + field + "ParamDateLTE,'DD/MM/YYYY'), " + field + ")";
            default:
                throw new RuntimeException("Unsupported operator " + queryComplexFilter.getOperator());
        }
    }

    public static Map.Entry<String, Object> buildSelectorMapping(QueryComplexFilter qcf){
        switch (qcf.getOperator()){
            case gte_date:
                return new AbstractMap.SimpleEntry<>(qcf.getField() + "ParamDateGTE", qcf.getValue());
            case lte_date:
                return new AbstractMap.SimpleEntry<>(qcf.getField() + "ParamDateLTE", qcf.getValue());
            default:
                return new AbstractMap.SimpleEntry<>(qcf.getField()+ "Param", qcf.getValue());
        }
    }


}
