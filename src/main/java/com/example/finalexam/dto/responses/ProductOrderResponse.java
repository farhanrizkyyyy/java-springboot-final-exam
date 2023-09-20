package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Integer price;

    public ProductOrderResponse(Product product) {
        this.code = product.getCode();
        this.name = product.getName();
        this.price = product.getPrice();
    }
}
