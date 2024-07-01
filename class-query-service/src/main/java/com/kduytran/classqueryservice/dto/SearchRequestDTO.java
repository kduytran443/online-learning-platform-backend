package com.kduytran.classqueryservice.dto;

import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.Data;

import java.util.List;

@Data
public class SearchRequestDTO {
    private static final int DEFAULT_SIZE = 100;
    private static final SortOrder DEFAULT_SORT_ORDER = SortOrder.Desc;
    private static final String DEFAULT_SORT_BY = "";
    private int page;
    private int size;
    private List<String> fields;
    private String searchTerm;
    private String sortBy;
    private SortOrder sortOrder;
    private List<String> categories;
    public int getSize() {
        return size != 0 ? size : DEFAULT_SIZE;
    }
    public SortOrder getSortOrder() {
        return sortOrder != null ? sortOrder : DEFAULT_SORT_ORDER;
    }
}
