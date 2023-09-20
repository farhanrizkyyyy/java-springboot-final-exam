package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Payment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("total_amount")
    private Integer totalAmount;

    @JsonProperty("total_paid")
    private Integer totalPaid;

    @JsonProperty("change")
    private Integer change;

    public PaymentResponse(Payment payment) {
        this.code = payment.getCode();
        this.totalAmount = payment.getTotalAmount();
        this.totalPaid = payment.getTotalPaid();
        this.change = payment.getChange();
    }
}
