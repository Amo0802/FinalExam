package com.example.FinalExam.servicesTest;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.exceptions.ProductNotFoundException;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.Region;
import com.example.FinalExam.product.services.DeleteProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    DeleteProductService deleteProductService;

    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setPrice(99.99);
        product.setManufacturer("Test Manufacturer");
        product.setCategory(category);
        product.setRegion(Region.US);
    }

    @Test
    void shouldDeleteProduct_WhenProductExists() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        deleteProductService.execute(productId);

        // Assert
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> deleteProductService.execute(productId));
        verify(productRepository, times(1)).findById(productId);
    }
}
