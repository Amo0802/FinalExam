package com.example.FinalExam.exceptions;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    PRODUCT_NOT_FOUND("Product Not Found"),
    NAME_REQUIRED("Name is required"),
    DESCRIPTION_LENGTH("Description must be 20 characters"),
    PRICE_CANNOT_BE_NEGATIVE("Price cannot be negative"),
    MANUFACTURER_REQUIRED("Manufacturer is required"),
    REGION_REQUIRED("Region is required");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}