package com.example.finalexam.services;

import com.example.finalexam.dto.requests.ProductRequest;
import com.example.finalexam.dto.responses.ProductResponse;
import com.example.finalexam.models.Category;
import com.example.finalexam.models.Product;
import com.example.finalexam.repositories.CategoryRepository;
import com.example.finalexam.repositories.ProductRepository;
import com.example.finalexam.utilities.Utility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<ProductResponse> getProducts() {
        ModelMapper mapper = new ModelMapper();
        List<Product> products = productRepository.findAllByDeletedAtIsNullOrderByCodeAsc();
        List<ProductResponse> responses = Arrays.asList(mapper.map(products, ProductResponse[].class));

        if (products.isEmpty()) responseMessage = "Product is empty";
        else responseMessage = "Fetch product success";

        return responses;
    }

    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        Category categoryTarget = categoryRepository.findOneByIdAndDeletedAtIsNull(categoryId);

        if (categoryTarget != null) {
            ModelMapper mapper = new ModelMapper();
            List<Product> products = productRepository.findByCategoryIdAndDeletedAtIsNull(categoryId);
            List<ProductResponse> responses = Arrays.asList(mapper.map(products, ProductResponse[].class));

            if (products.isEmpty()) responseMessage = "Product on category " + categoryTarget.getName() + " is empty";
            else responseMessage = "Fetch product success";

            return responses;
        } else {
            responseMessage = "Cannot find category with ID " + categoryId;
            return null;
        }

    }

    public ProductResponse getProductByCode(String code) {
        Product result = productRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (result != null) {
            responseMessage = "Fetch product success";
            return new ProductResponse(result);
        } else {
            responseMessage = "Cannot find product with code " + code.toUpperCase();
            return null;
        }
    }

    public ProductResponse createProduct(ProductRequest request) {
        String productName = Utility.capitalizeFirstLetter(request.getName());
        Category categoryTarget = categoryRepository.findOneByIdAndDeletedAtIsNull(request.getCategoryId());

        if (categoryTarget != null) {
            int qty = request.getQty();
            int price = request.getPrice();

            if (qty < 0) responseMessage = "Cannot assign negative number to product qty";
            else if (price < 0) responseMessage = "Cannot assign negative number to product price";
            else {
                Product newProduct = new Product(generateCode(), productName, request.getDescription(), request.getQty(), request.getPrice(), categoryTarget);
                productRepository.save(newProduct);
                responseMessage = "Product successfully added";

                return new ProductResponse(newProduct);
            }
        } else responseMessage = "Cannot find category with ID " + request.getCategoryId();

        return null;
    }

    public ProductResponse updateProduct(String code, ProductRequest request) {
        Product target = productRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());
        Category categoryTarget = categoryRepository.findOneByIdAndDeletedAtIsNull(request.getCategoryId());

        if (target != null) {
            if (categoryTarget != null) {
                int qty = request.getQty();
                int price = request.getPrice();

                if (qty < 0) responseMessage = "Cannot assign negative number to product qty";
                else if (price < 0) responseMessage = "Cannot assign negative number to product price";
                else {
                    String newProductName = Utility.capitalizeFirstLetter(request.getName());

                    target.setName(newProductName);
                    target.setDescription(request.getDescription());
                    target.setPrice(request.getPrice());
                    target.setQty(request.getQty());
                    responseMessage = "Product successfully updated";
                    productRepository.save(target);

                    return new ProductResponse(target);
                }
            } else responseMessage = "Cannot find category with ID " + request.getCategoryId();
        } else responseMessage = "Cannot find product with code " + code.toUpperCase();

        return null;
    }

    public ProductResponse deleteProduct(String code) {
        Product target = productRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (target != null) {
            target.setDeletedAt(new Date());
            productRepository.save(target);
            responseMessage = "Product successfully deleted";
            return new ProductResponse(target);
        } else {
            responseMessage = "Cannot find product with code " + code.toUpperCase();
            return null;
        }
    }

    private String generateCode() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) return "PR001";
        else {
            String productIdAsString = String.valueOf(products.get(products.size() - 1).getId());
            int lastproductId = Integer.parseInt(productIdAsString);
            lastproductId++;
            String codeDigit = String.format("%03d", lastproductId);

            return "PR" + codeDigit;
        }
    }
}
