package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeOrderResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;
}
