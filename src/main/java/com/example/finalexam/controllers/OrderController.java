package com.example.finalexam.controllers;

import com.example.finalexam.dto.requests.OrderRequest;
import com.example.finalexam.dto.responses.OrderResponse;
import com.example.finalexam.models.ApiResponse;
import com.example.finalexam.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getOrder(@RequestParam(required = false, name = "code") String code, @RequestParam(required = false, name = "employee_id") Long employeeId) {
        ApiResponse response;
        Object body;
        HttpStatus httpStatus;

        if (code == null && employeeId == null) {
            body = orderService.getOrders();
            httpStatus = HttpStatus.OK;
        } else if (code != null) {
            body = orderService.getOrderByCode(code);
            if (body == null) httpStatus = HttpStatus.NOT_FOUND;
            else httpStatus = HttpStatus.OK;
        } else {
            body = orderService.getOrdersByEmployeeId(employeeId);
            httpStatus = HttpStatus.OK;
        }

        response = new ApiResponse(orderService.getResponseMessage(), body);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @GetMapping("/date")
    public ResponseEntity<ApiResponse> getOrdersByDate(@RequestParam(name = "from") String from, @RequestParam(name = "until") String until) {
        List<OrderResponse> orders = orderService.getOrderByDateRange(from, until);
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), orders);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse newOrder = orderService.createOrder(request);
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), newOrder);
        HttpStatus httpStatus;

        if (newOrder == null) httpStatus = HttpStatus.BAD_REQUEST;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable String code) {
        OrderResponse target = orderService.deleteOrder(code);
        ApiResponse response = new ApiResponse(orderService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }
}