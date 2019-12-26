package com.infobelt.aadhaar.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * A filter for a query
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryComplexFilter {

    private QueryComplexLogic logic;
    private List<QueryComplexFilter> filters = new ArrayList<>();
    private String field;
    private QueryComplexOperator operator;
    private String value;
    private boolean ignoreCase;
    private String dataType;

}
