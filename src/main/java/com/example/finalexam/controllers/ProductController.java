package com.example.finalexam.controllers;

import com.example.finalexam.dto.requests.ProductRequest;
import com.example.finalexam.dto.responses.ProductResponse;
import com.example.finalexam.models.ApiResponse;
import com.example.finalexam.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getProduct(@RequestParam(required = false, name = "code") String code, @RequestParam(required = false, name = "category_id") Long categoryId) {
        ApiResponse response;
        Object body;
        HttpStatus httpStatus;

        if (code == null && categoryId == null) {
            body = productService.getProducts();
            httpStatus = HttpStatus.OK;
        } else if (code != null) {
            body = productService.getProductByCode(code);
            if (body == null) httpStatus = HttpStatus.NOT_FOUND;
            else httpStatus = HttpStatus.OK;
        } else {
            body = productService.getProductsByCategoryId(categoryId);
            httpStatus = HttpStatus.OK;
        }

        response = new ApiResponse(productService.getResponseMessage(), body);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductRequest request) {
        ProductResponse newProduct = productService.createProduct(request);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), newProduct);
        HttpStatus httpStatus;

        if (newProduct == null) httpStatus = HttpStatus.BAD_REQUEST;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable String code, @RequestBody ProductRequest request) {
        ProductResponse target = productService.updateProduct(code, request);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), target);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PatchMapping("/{code}")
    public ResponseEntity<ApiResponse> updateProductCategory(@PathVariable String code, @RequestBody ProductRequest request) {
        ProductResponse target = productService.updateProductCategory(code, request);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), target);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String code) {
        ProductResponse target = productService.deleteProduct(code);
        ApiResponse response = new ApiResponse(productService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }
}