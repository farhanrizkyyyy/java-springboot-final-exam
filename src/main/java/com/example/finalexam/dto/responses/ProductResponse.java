package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("qty")
    private Integer qty;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("category_id")
    private Long categoryId;

    public ProductResponse(Product product) {
        this.code = product.getCode();
        this.name = product.getName();
        this.description = product.getDescription();
        this.qty = product.getQty();
        this.price = product.getPrice();
        this.categoryId = product.getCategory() == null ? null : product.getCategory().getId();
    }
}
