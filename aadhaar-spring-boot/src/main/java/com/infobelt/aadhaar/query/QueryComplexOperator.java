package com.infobelt.aadhaar.query;

/**
 * Available operators
 */
public enum QueryComplexOperator {
    contains,
    doesnotcontain,
    endswith,
    startswith,
    eq,
    neq,
    gte,
    gt,
    lte,
    lt,
    isnull,
    isnotnull,
    isempty
}
