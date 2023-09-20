package com.example.finalexam.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDateRangeResponse {
    @JsonProperty("orders")
    private List<OrderResponse> orderResponses;

    @JsonProperty("total_income")
    private Integer totalIncome;
}
