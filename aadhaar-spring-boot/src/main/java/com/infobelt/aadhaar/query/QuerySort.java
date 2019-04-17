package com.infobelt.aadhaar.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Definition of a sort
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuerySort {

    private String columnName;
    private SortDirection direction = SortDirection.asc;

}
