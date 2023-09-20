package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Employee;
import com.example.finalexam.models.Order;
import com.example.finalexam.models.Payment;
import com.example.finalexam.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

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

    @JsonProperty("member")
    private Long memberId;

    @JsonProperty("employee")
    private EmployeeOrderResponse employee;

    @JsonProperty("payment")
    private PaymentResponse payment;

    @JsonProperty("products")
    private List<ProductOrderResponse> products;

    public OrderResponse(Order order) {
        this.code = order.getCode();
        this.orderDate = order.getOrderDate();
        this.payment = assignPayment(order.getPayment());
        this.employee = assignEmployee(order.getEmployee());
        this.products = assignProducts(order.getProducts());
        this.memberId = order.getMember() != null ? order.getMember().getId() : null;
    }

    private List<ProductOrderResponse> assignProducts(List<Product> products) {
        List<ProductOrderResponse> responses = new ArrayList<>();

        for (Product product : products) {
            ProductOrderResponse response = new ProductOrderResponse(product);
            responses.add(response);
        }

        return responses;
    }

    private PaymentResponse assignPayment(Payment payment) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(payment, PaymentResponse.class);
    }

    private EmployeeOrderResponse assignEmployee(Employee employee) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(employee, EmployeeOrderResponse.class);
    }
}
