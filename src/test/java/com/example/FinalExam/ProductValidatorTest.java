package com.example.FinalExam;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.exceptions.ErrorMessages;
import com.example.FinalExam.exceptions.ProductNotValidException;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.Region;
import com.example.FinalExam.product.validators.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductValidatorTest {

    private Product validProduct;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1);
        category.setName("Electronics");

        validProduct = new Product();
        validProduct.setId(UUID.randomUUID());
        validProduct.setName("Test Product");
        validProduct.setDescription("This is a test product description that is at least 20 characters long");
        validProduct.setPrice(99.99);
        validProduct.setManufacturer("Test Manufacturer");
        validProduct.setCategory(category);
        validProduct.setRegion(Region.US);
    }

    @Test
    void validate_ValidProduct_NoExceptionThrown() {
        // No exception should be thrown for a valid product
        assertDoesNotThrow(() -> ProductValidator.execute(validProduct));
    }

    @Test
    void validate_NullName_ThrowsException() {
        validProduct.setName(null);

        ProductNotValidException exception = assertThrows(
                ProductNotValidException.class,
                () -> ProductValidator.execute(validProduct)
        );

        assertEquals(ErrorMessages.NAME_REQUIRED.getMessage(), exception.getMessage());
    }

    @Test
    void validate_EmptyName_ThrowsException() {
        validProduct.setName("");

        ProductNotValidException exception = assertThrows(
                ProductNotValidException.class,
                () -> ProductValidator.execute(validProduct)
        );

        assertEquals(ErrorMessages.NAME_REQUIRED.getMessage(), exception.getMessage());
    }

    @Test
    void validate_ShortDescription_ThrowsException() {
        validProduct.setDescription("Too short");

        ProductNotValidException exception = assertThrows(
                ProductNotValidException.class,
                () -> ProductValidator.execute(validProduct)
        );

        assertEquals(ErrorMessages.DESCRIPTION_LENGTH.getMessage(), exception.getMessage());
    }

    @Test
    void validate_NullPrice_ThrowsException() {
        validProduct.setPrice(null);

        ProductNotValidException exception = assertThrows(
                ProductNotValidException.class,
                () -> ProductValidator.execute(validProduct)
        );

        assertEquals(ErrorMessages.PRICE_CANNOT_BE_NEGATIVE.getMessage(), exception.getMessage());
    }

    @Test
    void validate_NegativePrice_ThrowsException() {
        validProduct.setPrice(-10.0);

        ProductNotValidException exception = assertThrows(
                ProductNotValidException.class,
                () -> ProductValidator.execute(validProduct)
        );

        assertEquals(ErrorMessages.PRICE_CANNOT_BE_NEGATIVE.getMessage(), exception.getMessage());
    }

    @Test
    void validate_EmptyManufacturer_ThrowsException() {
        validProduct.setManufacturer("");

        ProductNotValidException exception = assertThrows(
                ProductNotValidException.class,
                () -> ProductValidator.execute(validProduct)
        );

        assertEquals(ErrorMessages.MANUFACTURER_REQUIRED.getMessage(), exception.getMessage());
    }

    @Test
    void validate_NullRegion_ThrowsException() {
        validProduct.setRegion(null);

        ProductNotValidException exception = assertThrows(
                ProductNotValidException.class,
                () -> ProductValidator.execute(validProduct)
        );

        assertEquals(ErrorMessages.REGION_REQUIRED.getMessage(), exception.getMessage());
    }
}
