package com.example.FinalExam.product.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateProductCommand {

    private UUID id;
    private Product product;

    public UpdateProductCommand(UUID id, Product product) {
        this.id = id;
        this.product = product;
    }
}
