package com.example.finalexam.services;

import com.example.finalexam.dto.requests.CategoryRequest;
import com.example.finalexam.dto.responses.CategoryResponse;
import com.example.finalexam.models.Category;
import com.example.finalexam.repositories.CategoryRepository;
import com.example.finalexam.utilities.Utility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<CategoryResponse> getCategories() {
        ModelMapper mapper = new ModelMapper();
        List<Category> categories = categoryRepository.findAllByDeletedAtIsNullOrderByCodeAsc();
        List<CategoryResponse> responses = Arrays.asList(mapper.map(categories, CategoryResponse[].class));

        if (categories.isEmpty()) responseMessage = "Category is empty";
        else responseMessage = "Fetch category success";

        return responses;
    }

    public CategoryResponse getCategoryByCode(String code) {
        Category result = categoryRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (result != null) {
            responseMessage = "Fetch category success";
            return new CategoryResponse(result);
        } else {
            responseMessage = "Cannot find category with code " + code.toUpperCase();
            return null;
        }

    }

    public CategoryResponse createCategory(CategoryRequest request) {
        String categoryName = Utility.capitalizeFirstLetter(request.getName());

        if (!isCategoryExist(request)) {
            responseMessage = "Category successfully added";
            Category newCategory = new Category(generateCode(), categoryName);
            categoryRepository.save(newCategory);

            return new CategoryResponse(newCategory);
        }

        return null;
    }

    public CategoryResponse updateCategory(String code, CategoryRequest request) {
        Category target = categoryRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (target != null) {
            String newCategoryName = Utility.capitalizeFirstLetter(request.getName());

            if (!isCategoryExist(request)) {
                responseMessage = "Category successfully updated";
                target.setName(newCategoryName);
                categoryRepository.save(target);

                return new CategoryResponse(target);
            }
        } else responseMessage = "Cannot find category with code " + code.toUpperCase();

        return null;
    }

    public CategoryResponse deleteCategory(String code) {
        Category target = categoryRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (target != null) {
            target.setDeletedAt(new Date());
            categoryRepository.save(target);
            responseMessage = "Category successfully deleted";
            return new CategoryResponse(target);
        } else {
            responseMessage = "Cannot find category with code " + code.toUpperCase();
            return null;
        }
    }

    public Boolean isCategoryExist(CategoryRequest request) {
        String categoryName = Utility.capitalizeFirstLetter(request.getName());

        if (categoryRepository.findOneByNameAndDeletedAtIsNull(categoryName) != null) {
            responseMessage = "Category " + categoryName + " already exist";
            return true;
        }

        return false;
    }

    private String generateCode() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) return "CG001";
        else {
            String categoryIdAsString = String.valueOf(categories.get(categories.size() - 1).getId());
            int lastCategoryId = Integer.parseInt(categoryIdAsString);
            lastCategoryId++;
            String codeDigit = String.format("%03d", lastCategoryId);

            return "CG" + codeDigit;
        }
    }
}
