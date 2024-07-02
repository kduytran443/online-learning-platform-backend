package com.kduytran.classqueryservice.dto;

import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchRequestDTO {
    private static final int DEFAULT_SIZE = 100;
    private static final SortOrder DEFAULT_SORT_ORDER = SortOrder.Desc;
    private int page;
    private int size;
    private List<String> fields = new ArrayList<>();
    private String searchTerm;
    private String sortBy;
    private SortOrder sortOrder;
    private List<String> sourceIncludes = new ArrayList<>();
    private List<String> sourceExcludes = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public int getSize() {
        return size != 0 ? size : DEFAULT_SIZE;
    }

    public SortOrder getSortOrder() {
        return sortOrder != null ? sortOrder : DEFAULT_SORT_ORDER;
    }

    public boolean hasSourceIncludes() {
        return sourceIncludes != null && !sourceIncludes.isEmpty();
    }

    public boolean hasSourceExcludes() {
        return sourceExcludes != null && !sourceExcludes.isEmpty();
    }

    public boolean hasSourceIncludesAndExcludes() {
        return hasSourceIncludes() && hasSourceExcludes();
    }

}
