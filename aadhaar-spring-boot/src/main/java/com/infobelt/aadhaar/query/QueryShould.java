package com.infobelt.aadhaar.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An element that should be included in a query.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryShould {
    private String columnName;
    private String value;
}
