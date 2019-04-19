package com.infobelt.aadhaar.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infobelt.aadhaar.query.*;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class QueryContextResolver implements HandlerMethodArgumentResolver {

    public static final int MAX_PAGE_SIZE = 10000;

    private ObjectMapper om = new ObjectMapper();

    private List<String> getParameter(NativeWebRequest req, String name) {
        if (req.getParameterValues(name) != null) {
            return new ArrayList(Arrays.asList(req.getParameterValues(name)));
        } else
            return new ArrayList<>();
    }

    private QueryFilter buildFilter(String p) {
        QueryFilter filter = new QueryFilter();

        // TODO clean this up
        if (p.split("=").length == 2) {
            filter.setColumnName(p.split("=")[0]);
            filter.setValue(p.split("=")[1]);
        }

        return filter;
    }

    private QuerySort buildSort(String p) {
        QuerySort sort = new QuerySort();

        if (p.contains(":")) {
            String[] parts = p.split(":");
            sort.setColumnName(parts[0]);
            sort.setDirection(SortDirection.valueOf(parts[1]));
        } else
            sort.setColumnName(p);

        return sort;
    }


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(QueryContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if (methodParameter.getParameterType().equals(QueryContext.class)) {
            QueryContext queryContext = new QueryContext();
            // We need to pull apart the request and fill in the query context
            getParameter(nativeWebRequest, "sort").forEach(p -> queryContext.getSorts().add(buildSort(p)));

            // We need to ensure we don't have the sort twice for the same column
            Set<String> sortedColumns = new HashSet<>();
            List<QuerySort> filteredSorts = new ArrayList<>();
            queryContext.getSorts().stream().filter(querySort -> !sortedColumns.contains(querySort.getColumnName())).forEach(querySort -> {
                filteredSorts.add(querySort);
                sortedColumns.add(querySort.getColumnName());
            });
            getParameter(nativeWebRequest, "filter").forEach(p -> queryContext.getFilters().add(buildFilter(p)));
            getParameter(nativeWebRequest, "page").stream().findFirst().ifPresent(page -> queryContext.setPage(Integer.parseInt(page)));
            getParameter(nativeWebRequest, "pageSize").stream().findFirst().ifPresent(pageSize -> {

                // The maximum size of a page is 10000
                if (Integer.parseInt(pageSize) > MAX_PAGE_SIZE)
                    queryContext.setPageSize(MAX_PAGE_SIZE);
                else
                    queryContext.setPageSize(Integer.parseInt(pageSize));
            });

            getParameter(nativeWebRequest, "include").forEach(include -> queryContext.getIncludes().add(include));
            getParameter(nativeWebRequest, "exclude").forEach(exclude -> queryContext.getExcludes().add(exclude));
            getParameter(nativeWebRequest, "complexFilter").forEach(cf -> {
                try {
                    queryContext.applyComplexQueryFilter(om.readValue(new ByteArrayInputStream(cf.getBytes()), QueryComplexFilter.class));
                } catch (IOException e) {
                    throw new RuntimeException("Unable to parse the complexFilter", e);
                }
            });

            return queryContext;
        }
        return null;
    }
}
