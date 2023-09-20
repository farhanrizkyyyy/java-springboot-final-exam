package com.example.finalexam.repositories;

import com.example.finalexam.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDeletedAtIsNullOrderByNameAsc();

    Employee findOneByCodeAndDeletedAtIsNull(String code);

    Employee findOneByIdAndDeletedAtIsNull(Long id);
}
