package com.example.finalexam.services;

import com.example.finalexam.dto.requests.EmployeeRequest;
import com.example.finalexam.dto.responses.EmployeeResponse;
import com.example.finalexam.models.Employee;
import com.example.finalexam.repositories.EmployeeRepository;
import com.example.finalexam.utilities.Utility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<EmployeeResponse> getEmployees() {
        ModelMapper mapper = new ModelMapper();
        List<Employee> employees = employeeRepository.findAllByDeletedAtIsNullOrderByNameAsc();
        List<EmployeeResponse> responses = Arrays.asList(mapper.map(employees, EmployeeResponse[].class));

        if (employees.isEmpty()) responseMessage = "Employee is empty";
        else responseMessage = "Fetch employee success";

        return responses;
    }

    public EmployeeResponse getEmployeeByCode(String code) {
        Employee result = employeeRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (result != null) {
            responseMessage = "Fetch employee success";
            return new EmployeeResponse(result);
        } else {
            responseMessage = "Cannot find employee with code " + code.toUpperCase();
            return null;
        }

    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        String employeeName = Utility.capitalizeFirstLetter(request.getName());

        Employee newEmployee = new Employee(generateCode(), employeeName, request.getPhone());
        employeeRepository.save(newEmployee);
        responseMessage = "Employee successfully added";

        return new EmployeeResponse(newEmployee);
    }

    public EmployeeResponse updateEmployee(String code, EmployeeRequest request) {
        Employee target = employeeRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (target != null) {
            String newEmployeeName = Utility.capitalizeFirstLetter(request.getName());

            target.setName(newEmployeeName);
            target.setPhone(request.getPhone());
            employeeRepository.save(target);
            responseMessage = "Employee successfully updated";

            return new EmployeeResponse(target);
        } else responseMessage = "Cannot find employee with code " + code.toUpperCase();

        return null;
    }

    public EmployeeResponse deleteEmployee(String code) {
        Employee target = employeeRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (target != null) {
            target.setDeletedAt(new Date());
            employeeRepository.save(target);
            responseMessage = "Employee successfully deleted";
            return new EmployeeResponse(target);
        } else {
            responseMessage = "Cannot find employee with code " + code.toUpperCase();
            return null;
        }
    }

    private String generateCode() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) return "EM001";
        else {
            String employeeIdAsString = String.valueOf(employees.get(employees.size() - 1).getId());
            int lastEmployeeId = Integer.parseInt(employeeIdAsString);
            lastEmployeeId++;
            String codeDigit = String.format("%03d", lastEmployeeId);

            return "EM" + codeDigit;
        }
    }
}
