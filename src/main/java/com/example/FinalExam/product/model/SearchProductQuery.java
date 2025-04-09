package com.example.FinalExam.product.model;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class SearchProductQuery {

    private String search;
    private String category;
    private Sort sort;

    public SearchProductQuery(String search, String category, Sort sort) {
        this.search = search;
        this.category = category;
        this.sort = sort;
    }
}
