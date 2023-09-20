package com.example.finalexam.seeders;

import com.example.finalexam.models.Product;
import com.example.finalexam.repositories.CategoryRepository;
import com.example.finalexam.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductSeeder {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void seed() {
        if (productRepository.findAll().isEmpty()) {
            List<Product> products = new ArrayList<>(Arrays.asList(
                    new Product("PR001", "Product 1", "Description 1", 20, 5_000, categoryRepository.findOneByIdAndDeletedAtIsNull(1L)),
                    new Product("PR002", "Product 2", "Description 2", 20, 10_000, categoryRepository.findOneByIdAndDeletedAtIsNull(2L)),
                    new Product("PR003", "Product 3", "Description 3", 20, 15_000, categoryRepository.findOneByIdAndDeletedAtIsNull(3L)),
                    new Product("PR004", "Product 4", "Description 4", 20, 20_000, categoryRepository.findOneByIdAndDeletedAtIsNull(4L)),
                    new Product("PR005", "Product 5", "Description 5", 20, 25_000, categoryRepository.findOneByIdAndDeletedAtIsNull(5L)),
                    new Product("PR006", "Product 6", "Description 6", 20, 30_000, categoryRepository.findOneByIdAndDeletedAtIsNull(1L)),
                    new Product("PR007", "Product 7", "Description 7", 20, 35_000, categoryRepository.findOneByIdAndDeletedAtIsNull(2L)),
                    new Product("PR008", "Product 8", "Description 8", 20, 45_000, categoryRepository.findOneByIdAndDeletedAtIsNull(3L)),
                    new Product("PR009", "Product 9", "Description 9", 20, 50_000, categoryRepository.findOneByIdAndDeletedAtIsNull(4L)),
                    new Product("PR010", "Product 10", "Description 10", 20, 55_000, categoryRepository.findOneByIdAndDeletedAtIsNull(5L)),
                    new Product("PR011", "Product 11", "Description 11", 20, 60_000, categoryRepository.findOneByIdAndDeletedAtIsNull(54L))
            ));

            productRepository.saveAll(products);
        }
    }
}
