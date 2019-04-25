package com.infobelt.aadhaar.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A context that can be used to refine a query (usually a listing) in a dynamic fashion
 */
@ApiModel(value = "QueryContext", description = "The context information for the query")
@Data
public class QueryContext {

    @Setter(AccessLevel.NONE)
    public final static int DEFAULT_PAGESIZE = 20;

    @ApiModelProperty(name = "pageSize", reference = "The number of instances of the resource to include in the page")
    private int pageSize = DEFAULT_PAGESIZE;
    @ApiModelProperty(name = "page", reference = "The page number that you wish to access")
    private int page = 1;
    @ApiModelProperty(name = "sorts", reference = "Sorts (attribute:direction where direction can be asc/desc)")
    private String sort;
    @ApiModelProperty(name = "filter", reference = "The filter to apply (ie. attribute=value)")
    private String filter;

    @Setter(AccessLevel.NONE)
    @ApiModelProperty(hidden = true)
    private List<QuerySort> sorts = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @ApiModelProperty(hidden = true)
    private List<QueryFilter> filters = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @ApiModelProperty(hidden = true)
    private List<String> includes = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @ApiModelProperty(hidden = true)
    private List<String> excludes = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @ApiModelProperty(hidden = true)
    private QueryComplexFilter queryComplexFilter;

    public void applyComplexQueryFilter(QueryComplexFilter complexFilter) {
        queryComplexFilter = complexFilter;
    }

    /**
     * Take this context and try and apply to to an object (for query by example approaches)
     *
     * @param object the object which we will try and apply the filters and sorts to
     * @return the updated object
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object applyToObject(Object object) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> filters = new HashMap<>();
        getFilters().forEach(f -> filters.put(f.getColumnName(), f.getValue()));

        // Lets add the sort by and sort dir if we need it
        getSorts().forEach(f -> filters.put("sortBy", f.getColumnName()));
        getSorts().forEach(f -> filters.put("sortDir", f.getDirection()).toString());

        BeanUtils.populate(object, filters);
        return object;
    }
}
