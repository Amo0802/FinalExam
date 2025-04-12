package com.example.FinalExam.product.model;

import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class SearchProductQuery {

    private String search;
    private String category;
    private Pageable pageable;

    public SearchProductQuery(String search, String category, Pageable pageable) {
        this.search = search;
        this.category = category;
        this.pageable = pageable;
    }
}
