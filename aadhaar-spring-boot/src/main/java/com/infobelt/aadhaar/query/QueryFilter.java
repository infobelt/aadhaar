package com.infobelt.aadhaar.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A filter for a query.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryFilter {

    private String columnName;
    private String value;

}
