package com.example.FinalExam;

import com.example.FinalExam.category.Category;
import com.example.FinalExam.category.CategoryRepository;
import com.example.FinalExam.product.ProductRepository;
import com.example.FinalExam.product.model.Product;
import com.example.FinalExam.product.model.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category electronicsCategory;
    private Category clothingCategory;
    private Product phone;
    private Product laptop;
    private Product tShirt;

    @BeforeEach
    void setUp() {
        // Clear repositories
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create categories
        electronicsCategory = new Category();
        electronicsCategory.setName("Electronics");
        electronicsCategory = categoryRepository.save(electronicsCategory);

        clothingCategory = new Category();
        clothingCategory.setName("Clothing");
        clothingCategory = categoryRepository.save(clothingCategory);

        // Create products
        phone = new Product();
        phone.setId(UUID.randomUUID());
        phone.setName("Smartphone X");
        phone.setDescription("A high-end smartphone with advanced features and capabilities");
        phone.setPrice(799.99);
        phone.setManufacturer("Tech Corp");
        phone.setCategory(electronicsCategory);
        phone.setRegion(Region.US);

        laptop = new Product();
        laptop.setId(UUID.randomUUID());
        laptop.setName("Laptop Pro");
        laptop.setDescription("Powerful laptop for professional use with long battery life");
        laptop.setPrice(1299.99);
        laptop.setManufacturer("Tech Corp");
        laptop.setCategory(electronicsCategory);
        laptop.setRegion(Region.CAN);

        tShirt = new Product();
        tShirt.setId(UUID.randomUUID());
        tShirt.setName("Cotton T-Shirt");
        tShirt.setDescription("Comfortable cotton t-shirt available in multiple colors");
        tShirt.setPrice(24.99);
        tShirt.setManufacturer("Fashion Brand");
        tShirt.setCategory(clothingCategory);
        tShirt.setRegion(Region.US);

        // Save products
        productRepository.saveAll(List.of(phone, laptop, tShirt));
    }

    @Test
    void searchByNameOrDescription_FindsByName() {
        // Pageable with pagination (page 0, size 10)
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> results = productRepository.searchByNameOrDescription(
                "Smartphone", "", pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals(phone.getId(), results.getContent().get(0).getId());
    }

    @Test
    void searchByNameOrDescription_FindsByDescription() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> results = productRepository.searchByNameOrDescription(
                "comfortable", "", pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals(tShirt.getId(), results.getContent().get(0).getId());
    }

    @Test
    void searchByNameOrDescription_FindsByNameAndCategory() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> results = productRepository.searchByNameOrDescription(
                "Pro", "Electronics", pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals(laptop.getId(), results.getContent().get(0).getId());
    }

    @Test
    void searchByNameOrDescription_EmptyTextReturnsAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> results = productRepository.searchByNameOrDescription(
                "", "", pageable);

        assertEquals(3, results.getTotalElements());
    }

    @Test
    void searchByCategory_FindsByCategory() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> results = productRepository.searchByCategory(
                "Electronics", pageable);

        assertEquals(2, results.getTotalElements());
        assertTrue(results.getContent().stream().anyMatch(p -> p.getId().equals(phone.getId())));
        assertTrue(results.getContent().stream().anyMatch(p -> p.getId().equals(laptop.getId())));
    }

    @Test
    void searchByCategory_EmptyCategoryReturnsAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> results = productRepository.searchByCategory(
                "", pageable);

        assertEquals(3, results.getTotalElements());
    }

    @Test
    void searchByCategory_CaseInsensitive() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> results = productRepository.searchByCategory(
                "electronics", pageable);

        assertEquals(2, results.getTotalElements());
    }

    @Test
    void searchByCategory_SortsByName() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());

        Page<Product> results = productRepository.searchByCategory(
                "Electronics", pageable);

        assertEquals(2, results.getTotalElements());
        assertEquals(laptop.getId(), results.getContent().get(0).getId());
        assertEquals(phone.getId(), results.getContent().get(1).getId());
    }
}