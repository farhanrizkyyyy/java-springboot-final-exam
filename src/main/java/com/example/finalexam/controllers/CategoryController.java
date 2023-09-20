package com.example.finalexam.controllers;

import com.example.finalexam.dto.requests.CategoryRequest;
import com.example.finalexam.dto.responses.CategoryResponse;
import com.example.finalexam.models.ApiResponse;
import com.example.finalexam.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getCategory(@RequestParam(required = false, name = "code") String code) {
        ApiResponse response;
        Object body;
        HttpStatus httpStatus;

        if (code == null) {
            body = categoryService.getCategories();
            httpStatus = HttpStatus.OK;
        } else {
            body = categoryService.getCategoryByCode(code);
            if (body == null) httpStatus = HttpStatus.NOT_FOUND;
            else httpStatus = HttpStatus.OK;
        }

        response = new ApiResponse(categoryService.getResponseMessage(), body);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse newCategory = categoryService.createCategory(request);
        ApiResponse response = new ApiResponse(categoryService.getResponseMessage(), newCategory);
        HttpStatus httpStatus;

        if (newCategory == null) httpStatus = HttpStatus.BAD_REQUEST;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PatchMapping("/{code}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable String code, @RequestBody CategoryRequest request) {
        CategoryResponse target = categoryService.updateCategory(code, request);
        ApiResponse response = new ApiResponse(categoryService.getResponseMessage(), target);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String code) {
        CategoryResponse target = categoryService.deleteCategory(code);
        ApiResponse response = new ApiResponse(categoryService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }
}