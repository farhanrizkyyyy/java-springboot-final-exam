package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Order;
import com.example.finalexam.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("order_date")
    private Date orderDate;

    @JsonProperty("total_amount")
    private Integer totalAmount;

    @JsonProperty("total_paid")
    private Integer totalPaid;

    @JsonProperty("change")
    private Integer change;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("employee")
    private EmployeeResponse employeeResponse;

    @JsonProperty("payment")
    private PaymentResponse paymentResponse;

    @JsonProperty("products")
    private List<ProductOrderResponse> productResponses;

    public OrderResponse(Order order) {
        this.code = order.getCode();
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.totalPaid = order.getPayment().getTotalPaid();
        this.change = order.getPayment().getChange();
        this.memberId = order.getMember() != null ? order.getMember().getId() : null;
        this.employeeResponse = new EmployeeResponse(order.getEmployee());
        this.paymentResponse = new PaymentResponse(order.getPayment());
        this.productResponses = assignProducts(order.getProducts());
    }

    private List<ProductOrderResponse> assignProducts(List<Product> products) {
        List<ProductOrderResponse> responses = new ArrayList<>();

        for (Product product : products) {
            ProductOrderResponse response = new ProductOrderResponse(product);
            responses.add(response);
        }

        return responses;
    }
}
