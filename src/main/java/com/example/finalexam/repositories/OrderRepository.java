package com.example.finalexam.repositories;

import com.example.finalexam.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    List<Order> findByEmployeeIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long employeeId);

    List<Order> findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long memberId);

    List<Order> findByDeletedAtIsNullAndOrderDateBetween(LocalDate date1, LocalDate date2);

    List<Order> findByMemberIdAndDeletedAtIsNullAndOrderDateBetween(Long memberId, LocalDate date1, LocalDate date2);

    Order findOneByCodeAndDeletedAtIsNull(String code);

}
