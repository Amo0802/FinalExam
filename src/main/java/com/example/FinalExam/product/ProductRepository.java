package com.example.FinalExam.product;

import com.example.FinalExam.product.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

//  List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchName, String searchDescription);

//  CONTACT is better to use, % matching parts of the string, don't have to match exactly whole string.
    @Query("""
    SELECT p FROM Product p
    WHERE
        (:text = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%'))
        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%')))
    AND
        (:category = '' OR LOWER(p.category.name) = LOWER(:category))
    """)
    List<Product> searchByNameOrDescription(
            @Param("text") String text,
            @Param("category") String category,
            Sort sort
    );

    @Query("""
    SELECT p FROM Product p
    WHERE
        (:category = '' OR LOWER(p.category.name) = LOWER(:category))
    """)
    List<Product> searchByCategory(
            @Param("category") String category,
            Sort sort
    );

//  List<Product> findByCategory_Name(String categoryName);
}
