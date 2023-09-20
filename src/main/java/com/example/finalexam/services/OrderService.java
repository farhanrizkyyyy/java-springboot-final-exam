package com.example.finalexam.services;

import com.example.finalexam.dto.requests.OrderRequest;
import com.example.finalexam.dto.responses.OrderResponse;
import com.example.finalexam.models.Employee;
import com.example.finalexam.models.Order;
import com.example.finalexam.models.Payment;
import com.example.finalexam.models.Product;
import com.example.finalexam.repositories.EmployeeRepository;
import com.example.finalexam.repositories.OrderRepository;
import com.example.finalexam.repositories.PaymentRepository;
import com.example.finalexam.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<OrderResponse> getOrders() {
        ModelMapper mapper = new ModelMapper();
        List<Order> orders = orderRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
        List<OrderResponse> responses = Arrays.asList(mapper.map(orders, OrderResponse[].class));

        if (orders.isEmpty()) responseMessage = "Order is empty";
        else responseMessage = "Fetch order success";

        return responses;
    }

    public List<OrderResponse> getOrdersByEmployeeId(Long employeeId) {
        Employee employeeTarget = employeeRepository.findOneByIdAndDeletedAtIsNull(employeeId);

        if (employeeTarget != null) {
            ModelMapper mapper = new ModelMapper();
            List<Order> orders = orderRepository.findByEmployeeIdAndDeletedAtIsNullOrderByCreatedAtDesc(employeeId);
            List<OrderResponse> responses = Arrays.asList(mapper.map(orders, OrderResponse[].class));

            if (orders.isEmpty()) responseMessage = "Order is empty";
            else responseMessage = "Fetch order success";

            return responses;
        } else responseMessage = "Cannot find employee with ID " + employeeId;

        return null;
    }

    public OrderResponse getOrderByCode(String code) {
        Order result = orderRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (result != null) {
            responseMessage = "Fetch order success";
            return new OrderResponse(result);
        } else {
            responseMessage = "Cannot find order with code " + code.toUpperCase();
            return null;
        }
    }

    public OrderResponse createOrder(OrderRequest request) {
        int totalAmount = request.getTotalAmount();
        int totalPaid = request.getTotalPaid();
        Employee employeeTarget = employeeRepository.findOneByIdAndDeletedAtIsNull(request.getEmployeeId());
        List<Product> products = new ArrayList<>();
        boolean isProductExist = true;

        for (int i = 0; i < request.getProductIds().size(); i++) {
            Long productId = request.getProductIds().get(i);
            Product product = productRepository.findOneByIdAndDeletedAtIsNull(productId);

            if (product != null) products.add(product);
            else {
                isProductExist = false;
                responseMessage = "Cannot find product with ID " + productId;
            }

            if (!isProductExist) break;
        }

        if (employeeTarget != null) {
            if (totalAmount > 0) {
                if (totalPaid >= totalAmount) {
                    int change = totalPaid - totalAmount;

                    if (!isProductExist) return null;
                    else {
                        Order newOrder = new Order(generateCode("order"), request.getTotalAmount(), null, employeeTarget, products);
                        Payment newPayment = new Payment(generateCode("payment"), totalAmount, totalPaid, change, newOrder);

                        newOrder.setPayment(newPayment);

                        orderRepository.save(newOrder);
                        paymentRepository.save(newPayment);
                        responseMessage = "Order successfully created";

                        return new OrderResponse(newOrder);
                    }
                } else responseMessage = "Total paid must greater than or equals " + totalAmount;
            } else responseMessage = "Total amount must be greater than 0";
        } else responseMessage = "Cannot find employee with ID " + request.getEmployeeId();

        return null;
    }

    public OrderResponse deleteOrder(String code) {
        Order target = orderRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (target != null) {
            Payment paymentTarget = target.getPayment();
            paymentTarget.setDeletedAt(new Date());
            target.setDeletedAt(new Date());
            orderRepository.save(target);
            paymentRepository.save(paymentTarget);
            responseMessage = "Order successfully deleted";
            return new OrderResponse(target);
        } else {
            responseMessage = "Cannot find order with code " + code.toUpperCase();
            return null;
        }
    }

    private String generateCode(String type) {
        if (type.equals("payment")) {
            List<Payment> payments = paymentRepository.findAll();

            if (payments.isEmpty()) return "PY001";
            else {
                String paymentIdAsString = String.valueOf(payments.get(payments.size() - 1).getId());
                int lastPaymentId = Integer.parseInt(paymentIdAsString);
                lastPaymentId++;
                String codeDigit = String.format("%03d", lastPaymentId);

                return "PY" + codeDigit;
            }
        }

        if (type.equals("order")) {
            List<Order> orders = orderRepository.findAll();

            if (orders.isEmpty()) return "OR001";
            else {
                String orderIdAsString = String.valueOf(orders.get(orders.size() - 1).getId());
                int lastOrderId = Integer.parseInt(orderIdAsString);
                lastOrderId++;
                String codeDigit = String.format("%03d", lastOrderId);

                return "OR" + codeDigit;
            }
        }

        return "";
    }
}
