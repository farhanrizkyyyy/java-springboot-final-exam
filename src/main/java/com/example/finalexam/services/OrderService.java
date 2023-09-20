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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        seed();

        ModelMapper mapper = new ModelMapper();
        List<Order> orders = orderRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
        List<OrderResponse> responses = Arrays.asList(mapper.map(orders, OrderResponse[].class));

        if (orders.isEmpty()) responseMessage = "Order is empty";
        else responseMessage = "Fetch order success";

        return responses;
    }

    public List<OrderResponse> getOrderByDateRange(String date1, String date2) {
        ModelMapper mapper = new ModelMapper();
        LocalDate from = LocalDate.parse(date1, DateTimeFormatter.ofPattern(("yyyy-MM-dd")));
        LocalDate until = LocalDate.parse(date2, DateTimeFormatter.ofPattern(("yyyy-MM-dd")));
        List<Order> orders = orderRepository.findByDeletedAtIsNullAndOrderDateBetween(from, until);
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
        boolean isProductAvailable = true;

        for (int i = 0; i < request.getProductIds().size(); i++) {
            Long productId = request.getProductIds().get(i);
            Product product = productRepository.findOneByIdAndDeletedAtIsNull(productId);

            if (product != null) {
                if (product.getQty() == 0) {
                    isProductAvailable = false;
                    responseMessage = product.getName() + " qty is 0";
                } else {
                    products.add(product);
                    product.setQty(product.getQty() - 1);
                    productRepository.save(product);
                }
            } else {
                isProductExist = false;
                responseMessage = "Cannot find product with ID " + productId;
            }

            if (!isProductExist) break;
            if (!isProductAvailable) break;
        }

        if (employeeTarget != null) {
            if (totalAmount > 0) {
                if (totalPaid >= totalAmount) {
                    int change = totalPaid - totalAmount;

                    if (!isProductExist) return null;
                    else if (!isProductAvailable) return null;
                    else {
                        Order newOrder = new Order(generateCode("order"), request.getTotalAmount(), null, employeeTarget, products, null);
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

    private void seed() {
        if (orderRepository.findAll().isEmpty()) {
            Product product1 = productRepository.findOneByIdAndDeletedAtIsNull(1L);
            Product product2 = productRepository.findOneByIdAndDeletedAtIsNull(2L);
            Product product3 = productRepository.findOneByIdAndDeletedAtIsNull(3L);
            Product product4 = productRepository.findOneByIdAndDeletedAtIsNull(4L);
            Product product5 = productRepository.findOneByIdAndDeletedAtIsNull(5L);
            Product product6 = productRepository.findOneByIdAndDeletedAtIsNull(6L);
            Product product7 = productRepository.findOneByIdAndDeletedAtIsNull(7L);
            Product product8 = productRepository.findOneByIdAndDeletedAtIsNull(8L);
            Product product9 = productRepository.findOneByIdAndDeletedAtIsNull(9L);
            Product product10 = productRepository.findOneByIdAndDeletedAtIsNull(10L);

            List<Order> orders = new ArrayList<>(Arrays.asList(
                    new Order("OR001", 15000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(1L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY001")),
                    new Order("OR002", 30000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(4L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY002")),
                    new Order("OR003", 30000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(2L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY003")),
                    new Order("OR004", 110000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(1L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY004")),
                    new Order("OR005", 45000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(8L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY005")),
                    new Order("OR006", 15000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(7L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY006")),
                    new Order("OR007", 85000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(5L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY007")),
                    new Order("OR008", 110000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(4L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY008")),
                    new Order("OR009", 70000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(2L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY009")),
                    new Order("OR010", 40000, null, employeeRepository.findOneByIdAndDeletedAtIsNull(1L), new ArrayList<>(), paymentRepository.findOneByCodeAndDeletedAtIsNull("PY010"))
            ));

            List<Payment> payments = new ArrayList<>(Arrays.asList(
                    new Payment("PY001", 15000, 20000, 5000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR001")),
                    new Payment("PY002", 30000, 30000, 0, orderRepository.findOneByCodeAndDeletedAtIsNull("OR002")),
                    new Payment("PY003", 30000, 50000, 20000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR003")),
                    new Payment("PY004", 110000, 150000, 40000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR004")),
                    new Payment("PY005", 45000, 50000, 5000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR005")),
                    new Payment("PY006", 15000, 15000, 0, orderRepository.findOneByCodeAndDeletedAtIsNull("OR006")),
                    new Payment("PY007", 85000, 100000, 15000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR007")),
                    new Payment("PY008", 110000, 120000, 10000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR008")),
                    new Payment("PY009", 70000, 100000, 30000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR009")),
                    new Payment("PY010", 40000, 50000, 10000, orderRepository.findOneByCodeAndDeletedAtIsNull("OR010"))
            ));

            // set payment
            orders.get(0).setPayment(payments.get(0));
            orders.get(1).setPayment(payments.get(1));
            orders.get(2).setPayment(payments.get(2));
            orders.get(3).setPayment(payments.get(3));
            orders.get(4).setPayment(payments.get(4));
            orders.get(5).setPayment(payments.get(5));
            orders.get(6).setPayment(payments.get(6));
            orders.get(7).setPayment(payments.get(7));
            orders.get(8).setPayment(payments.get(8));
            orders.get(9).setPayment(payments.get(9));

            // add product to order
            orders.get(0).getProducts().add(product1);
            orders.get(0).getProducts().add(product2);

            orders.get(1).getProducts().add(product2);
            orders.get(1).getProducts().add(product4);

            orders.get(2).getProducts().add(product1);
            orders.get(2).getProducts().add(product2);
            orders.get(2).getProducts().add(product3);

            orders.get(3).getProducts().add(product5);
            orders.get(3).getProducts().add(product6);
            orders.get(3).getProducts().add(product10);

            orders.get(4).getProducts().add(product1);
            orders.get(4).getProducts().add(product2);
            orders.get(4).getProducts().add(product5);

            orders.get(5).getProducts().add(product1);
            orders.get(5).getProducts().add(product2);

            orders.get(6).getProducts().add(product4);
            orders.get(6).getProducts().add(product5);
            orders.get(6).getProducts().add(product9);

            orders.get(7).getProducts().add(product6);
            orders.get(7).getProducts().add(product7);
            orders.get(7).getProducts().add(product8);

            orders.get(8).getProducts().add(product1);
            orders.get(8).getProducts().add(product2);
            orders.get(8).getProducts().add(product10);

            orders.get(9).getProducts().add(product1);
            orders.get(9).getProducts().add(product3);
            orders.get(9).getProducts().add(product4);

            // add order to product
            product1.getOrders().add(orders.get(0));
            product1.getOrders().add(orders.get(2));
            product1.getOrders().add(orders.get(4));
            product1.getOrders().add(orders.get(5));
            product1.getOrders().add(orders.get(8));
            product1.getOrders().add(orders.get(9));

            product2.getOrders().add(orders.get(0));
            product2.getOrders().add(orders.get(1));
            product2.getOrders().add(orders.get(2));
            product2.getOrders().add(orders.get(4));
            product2.getOrders().add(orders.get(5));
            product2.getOrders().add(orders.get(8));

            product3.getOrders().add(orders.get(2));
            product3.getOrders().add(orders.get(9));

            product4.getOrders().add(orders.get(1));
            product4.getOrders().add(orders.get(6));
            product4.getOrders().add(orders.get(9));

            product5.getOrders().add(orders.get(3));
            product5.getOrders().add(orders.get(4));
            product5.getOrders().add(orders.get(6));

            product6.getOrders().add(orders.get(3));
            product6.getOrders().add(orders.get(7));

            product7.getOrders().add(orders.get(7));

            product8.getOrders().add(orders.get(7));

            product9.getOrders().add(orders.get(6));

            product10.getOrders().add(orders.get(3));
            product10.getOrders().add(orders.get(8));

            orderRepository.saveAll(orders);
            paymentRepository.saveAll(payments);
        }
    }
}
