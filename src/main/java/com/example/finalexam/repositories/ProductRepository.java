package com.example.finalexam.repositories;

import com.example.finalexam.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByDeletedAtIsNullOrderByCodeAsc();

    List<Product> findByNameAndDeletedAtIsNull(String name);

    List<Product> findByCategoryIdAndDeletedAtIsNull(Long categoryId);

    Product findOneByCodeAndDeletedAtIsNull(String code);
}
