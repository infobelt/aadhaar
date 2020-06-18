package com.infobelt.aadhaar.utils;

import com.infobelt.aadhaar.query.QueryComplexFilter;
import com.infobelt.aadhaar.query.QueryFilter;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.Map;

public class SqlUtil {

    public final static String QUERY_DATE_PATTERN = "dd/MM/yyyy";

    public static Query getNativeAllQuery(EntityManager entityManager, StringBuilder query) {
        return
                entityManager
                        .createNativeQuery(query.toString())
                        .setFlushMode(FlushModeType.COMMIT);
    }

    public static Query getMappedQuery(EntityManager entityManager, StringBuilder query, Pageable pageable, String resultSetName){

        Query executeQuery = entityManager
                .createNativeQuery(query.toString(), resultSetName)
                .setFlushMode(FlushModeType.COMMIT);

        //pageable would be null if we were returning all results
        if(pageable != null){
            executeQuery.setMaxResults(pageable.getPageSize());
            executeQuery.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());

        }
        return executeQuery;
    }

    public static String buildWhereFromSimpleFilter(QueryFilter queryFilter) {
        String field = queryFilter.getColumnName();
        return " LOWER(" + field + ") like COALESCE(LOWER(:" + field + "Param), LOWER(" + field + "))";
    }

    public static String buildWhereFromComplexFilter(QueryComplexFilter queryComplexFilter) {
        String field = queryComplexFilter.getField();
        String dataType = queryComplexFilter.getDataType();
        switch(queryComplexFilter.getOperator()) {
            case eq:
                if (dataType != null && dataType.equals("clob")) {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like LOWER(:" + field + "Param)";
                    }
                    return " " + field + " like :" + field + "Param";
                } else {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like COALESCE(LOWER(:" + field + "Param), LOWER(" + field + "))";
                    }
                    return " " + field + " like COALESCE(:" + field + "Param, " + field + ")";
                }
            case neq:
                if (dataType != null && dataType.equals("clob")) {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") not like LOWER(:" + field + "Param)";
                    }
                    return " " + field + " not like :" + field + "Param";
                } else {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") != COALESCE(LOWER(:" + field + "Param), LOWER(" + field + "))";
                    }
                    return " " + field + " != COALESCE(:" + field + "Param, " + field + ")";
                }
            case contains:
                if (dataType != null && dataType.equals("clob")) {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like LOWER(CONCAT(CONCAT('%',:" + field + "Param), '%'))";
                    }
                    return " " + field + " like CONCAT(CONCAT('%',:" + field + "Param), '%')";
                } else {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like COALESCE(LOWER(CONCAT(CONCAT('%',:" + field + "Param), '%')), LOWER(" + field + "))";
                    }
                    return " " + field + " like COALESCE(CONCAT(CONCAT('%',:" + field + "Param), '%'), " + field + ")";
                }
            case doesnotcontain:
                if (dataType != null && dataType.equals("clob")) {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") not like LOWER(CONCAT(CONCAT('%',:" + field + "Param), '%'))";
                    }
                    return " " + field + " not like CONCAT(CONCAT('%',:" + field + "Param), '%')";
                } else {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") not like COALESCE(LOWER(CONCAT(CONCAT('%',:" + field + "Param), '%')), LOWER(" + field + "))";
                    }
                    return " " + field + " not like COALESCE(CONCAT(CONCAT('%',:" + field + "Param), '%'), " + field + ")";
                }
            case startswith:
                if (dataType != null && dataType.equals("clob")) {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like LOWER(CONCAT(:" + field + "Param, '%'))";
                    }
                    return " " + field + " like CONCAT(:" + field + "Param, '%')";
                } else {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like COALESCE(LOWER(CONCAT(:" + field + "Param, '%')), LOWER(" + field + "))";
                    }
                    return " " + field + " like COALESCE(CONCAT(:" + field + "Param, '%'), " + field + ")";
                }
            case endswith:
                if (dataType != null && dataType.equals("clob")) {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like LOWER(CONCAT('%', :" + field + "Param))";
                    }
                    return " " + field + " like CONCAT('%', :" + field + "Param)";
                } else {
                    if (queryComplexFilter.isIgnoreCase()) {
                        return " LOWER(" + field + ") like COALESCE(LOWER(CONCAT('%', :" + field + "Param)), LOWER(" + field + "))";
                    }
                    return " " + field + " like COALESCE(CONCAT('%', :" + field + "Param), " + field + ")";
                }
            case gte:
                return " " + field + " >= COALESCE(:" + field + "Param, " + field + ")";
            case gt:
                return " " + field + " > COALESCE(:" + field + "Param, " + field + ")";
            case lte:
                return " " + field + " <= COALESCE(:" + field + "Param, " + field + ")";
            case lt:
                return " " + field + " < COALESCE(:" + field + "Param, " + field + ")";
            case gte_date:
                return " trunc(" + field + ") >= COALESCE(TO_DATE(:" + field + "ParamDateGTE,'" + QUERY_DATE_PATTERN + "'), " + field + ")";
            case lte_date:
                return " trunc(" + field + ") <= COALESCE(TO_DATE(:" + field + "ParamDateLTE,'" + QUERY_DATE_PATTERN + "'), " + field + ")";
            case isnull:
                return " " + field + " is null";
            case isnotnull:
                return " " + field + " is not null";
            default:
                throw new RuntimeException("Unsupported operator " + queryComplexFilter.getOperator());
        }
    }

    public static Map.Entry<String, Object> buildSimpleSelectorMapping(String fieldName, String value) {
        return new AbstractMap.SimpleEntry<>(fieldName + "Param", value);
    }

    public static Map.Entry<String, Object> buildSelectorMapping(QueryComplexFilter qcf){
        switch (qcf.getOperator()){
            case gte_date:
                return new AbstractMap.SimpleEntry<>(qcf.getField() + "ParamDateGTE", convertDateForSearch(qcf.getValue()));
            case lte_date:
                return new AbstractMap.SimpleEntry<>(qcf.getField() + "ParamDateLTE", convertDateForSearch(qcf.getValue()));
            default:
                return new AbstractMap.SimpleEntry<>(qcf.getField()+ "Param", qcf.getValue());
        }
    }

    private static String convertDateForSearch(String dateString){
        //First, decode date - date is encoded to prevent the lost of '+'
        dateString = dateString.replace("%2B", "+");
        try{
            ZonedDateTime parsedVal;
            parsedVal = ZonedDateTime.parse(dateString);
            return parsedVal.format(DateTimeFormatter.ofPattern(QUERY_DATE_PATTERN));
        }
        catch(DateTimeParseException e){
        }
        try{
            LocalDate parsedVal;
            parsedVal = LocalDate.parse(dateString);
            return parsedVal.format(DateTimeFormatter.ofPattern(QUERY_DATE_PATTERN));
        }
        catch(DateTimeParseException e){
        }
        return null;
    }
}
