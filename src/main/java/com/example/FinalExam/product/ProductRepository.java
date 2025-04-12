package com.example.FinalExam.product;

import com.example.FinalExam.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

//  List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchName, String searchDescription);

//  CONTACT is better to use, % matching parts of the string, don't have to match exactly whole string.
    @Query("""
        SELECT p FROM Product p
        WHERE
            (:text IS NULL OR :text = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%'))
            OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%')))
        AND
            (:category IS NULL OR :category = '' OR LOWER(p.category.name) = LOWER(:category))
        """)
    Page<Product> searchByNameOrDescription(
        @Param("text") String text,
        @Param("category") String category,
        Pageable pageable
    );

    @Query("""
        SELECT p FROM Product p
        WHERE
            (:category IS NULL OR :category = '' OR LOWER(p.category.name) = LOWER(:category))
        """)
    Page<Product> searchByCategory(
        @Param("category") String category,
        Pageable pageable
    );

//  List<Product> findByCategory_Name(String categoryName);
}
