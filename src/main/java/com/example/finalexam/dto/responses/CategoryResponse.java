package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    public CategoryResponse(Category category) {
        this.code = category.getCode();
        this.name = category.getName();
    }
}
