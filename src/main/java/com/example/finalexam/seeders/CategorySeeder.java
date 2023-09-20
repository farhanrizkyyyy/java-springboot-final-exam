package com.example.finalexam.seeders;

import com.example.finalexam.models.Category;
import com.example.finalexam.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CategorySeeder {
    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void seed() {
        if (categoryRepository.findAll().isEmpty()) {
            List<Category> categories = new ArrayList<>(Arrays.asList(
                    new Category("CG001", "Food"),
                    new Category("CG002", "Beverage"),
                    new Category("CG003", "Snack"),
                    new Category("CG004", "Accessory"),
                    new Category("CG005", "Tool"),
                    new Category("CG006", "Book"),
                    new Category("CG007", "Bag"),
                    new Category("CG008", "Toy"),
                    new Category("CG009", "Clothing"),
                    new Category("CG010", "Medicine")
            ));

            categoryRepository.saveAll(categories);
        }
    }
}