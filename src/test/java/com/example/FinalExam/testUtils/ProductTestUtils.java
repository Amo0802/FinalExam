package com.example.FinalExam.testUtils;

import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.ProductDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTestUtils {

    public static void assertProductMatchesDTO(Product expected, ProductDTO actual) {
        assertEquals(expected.getId(), actual.getId(), "ID mismatch");
        assertEquals(expected.getName(), actual.getName(), "Name mismatch");
        assertEquals(expected.getDescription(), actual.getDescription(), "Description mismatch");
        assertEquals(expected.getPrice(), actual.getPrice(), "Price mismatch");
        assertEquals(expected.getManufacturer(), actual.getManufacturer(), "Manufacturer mismatch");
        assertEquals(expected.getRegion(), actual.getRegion(), "Region mismatch");
        assertEquals(expected.getCategory().getId(), actual.getCategory().getId(), "Category ID mismatch");
        assertEquals(expected.getCategory().getName(), actual.getCategory().getName(), "Category name mismatch");

    }
}
