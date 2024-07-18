package com.kduytran.classqueryservice.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class SearchRequestDTO {
    public static final int DEFAULT_SIZE = 8;
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;
    private int page;
    private int size;
    private String searchTerm;
    private String sortBy;
    private Sort.Direction direction;
    private List<String> categories;
    private Double minAverageRating;
    private Double maxAverageRating;

    public Sort.Direction getDirection() {
        return direction != null ? direction : DEFAULT_SORT_DIRECTION;
    }

    public double getMinAverageRating() {
        return minAverageRating != null ? minAverageRating : 0;
    }

    public double getMaxAverageRating() {
        return maxAverageRating != null ? maxAverageRating : 5;
    }

}
