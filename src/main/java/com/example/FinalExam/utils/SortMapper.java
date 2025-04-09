package com.example.FinalExam.utils;

import org.springframework.data.domain.Sort;

public class SortMapper {
    public static Sort map(String sortBy) {
        return switch (sortBy) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "name" -> Sort.by("name").ascending();
            case "name_desc" -> Sort.by("name").descending();
            default -> Sort.unsorted();
        };
    }
}
