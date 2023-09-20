package com.example.finalexam.dto.requests;

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
public class OrderRequest {
    @JsonProperty("total_amount")
    private Integer totalAmount;

    @JsonProperty("total_paid")
    private Integer totalPaid;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("employee_id")
    private Long employeeId;

    @JsonProperty("product_ids")
    private List<Long> productIds;
}
