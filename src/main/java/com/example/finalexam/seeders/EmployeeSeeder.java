package com.example.finalexam.seeders;

import com.example.finalexam.models.Employee;
import com.example.finalexam.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EmployeeSeeder {
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void seed() {
        if (employeeRepository.findAll().isEmpty()) {
            List<Employee> employees = new ArrayList<>(Arrays.asList(
                    new Employee("EM001", "Employee 1", "123123123"),
                    new Employee("EM002", "Employee 2", "123123123"),
                    new Employee("EM003", "Employee 3", "123123123"),
                    new Employee("EM004", "Employee 4", "123123123"),
                    new Employee("EM005", "Employee 5", "123123123"),
                    new Employee("EM006", "Employee 6", "123123123"),
                    new Employee("EM007", "Employee 7", "123123123"),
                    new Employee("EM008", "Employee 8", "123123123"),
                    new Employee("EM009", "Employee 9", "123123123"),
                    new Employee("EM010", "Employee 10", "123123123")
            ));

            employeeRepository.saveAll(employees);
        }
    }
}
