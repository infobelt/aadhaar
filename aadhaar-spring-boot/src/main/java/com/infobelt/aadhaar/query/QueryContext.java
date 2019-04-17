package com.infobelt.aadhaar.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A context that can be used to refine a query (usually a listing) in a dynamic fashion
 */
@ApiModel(value = "QueryContext", description = "The context information for the query")
@Data
public class QueryContext {

    @Setter(AccessLevel.NONE)
    public final static int DEFAULT_PAGESIZE = 20;

    @ApiModelProperty(name = "The number of entries in a page")
    private int pageSize = DEFAULT_PAGESIZE;

    @ApiModelProperty(name = "The page number")
    private int page = 1;

    @ApiModelProperty(name = "The sorts to apply", reference = "In the form attribute:direction where direction can be asc/desc")
    private String sort;

    @ApiModelProperty(name = "The filters to apply", reference = "In the form filter=value")
    private String filter;
    @Setter(AccessLevel.NONE)
    private List<QuerySort> sorts = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    private List<QueryFilter> filters = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    private List<String> includes = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    private List<String> excludes = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    private QueryComplexFilter queryComplexFilter;

    public void applyComplexQueryFilter(QueryComplexFilter complexFilter) {
        queryComplexFilter = complexFilter;
    }
}
