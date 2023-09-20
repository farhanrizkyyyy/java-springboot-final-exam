package com.example.finalexam.repositories;

import com.example.finalexam.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    List<Order> findByEmployeeIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long employeeId);

    List<Order> findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long memberId);

    Order findOneByCodeAndDeletedAtIsNull(String code);

}
