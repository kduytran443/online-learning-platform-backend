package com.kduytran.classqueryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDTO<T> {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> items;
}
