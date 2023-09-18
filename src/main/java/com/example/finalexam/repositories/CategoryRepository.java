package com.example.finalexam.repositories;

import com.example.finalexam.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDeletedAtIsNullOrderByNameAsc();
}
