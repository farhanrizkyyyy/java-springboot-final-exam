package com.example.finalexam.controllers;

import com.example.finalexam.dto.requests.CategoryRequest;
import com.example.finalexam.dto.requests.EmployeeRequest;
import com.example.finalexam.dto.responses.CategoryResponse;
import com.example.finalexam.dto.responses.EmployeeResponse;
import com.example.finalexam.models.ApiResponse;
import com.example.finalexam.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getEmployee(@RequestParam(required = false, name = "code") String code) {
        ApiResponse response;
        Object body;
        HttpStatus httpStatus;

        if (code == null) {
            body = employeeService.getEmployees();
            httpStatus = HttpStatus.OK;
        } else {
            body = employeeService.getEmployeeByCode(code);
            if (body == null) httpStatus = HttpStatus.NOT_FOUND;
            else httpStatus = HttpStatus.OK;
        }

        response = new ApiResponse(employeeService.getResponseMessage(), body);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody EmployeeRequest request) {
        EmployeeResponse newEmployee = employeeService.createEmployee(request);
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), newEmployee);
        HttpStatus httpStatus;

        if (newEmployee == null) httpStatus = HttpStatus.BAD_REQUEST;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable String code, @RequestBody EmployeeRequest request) {
        EmployeeResponse target = employeeService.updateEmployee(code, request);
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), target);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String code) {
        EmployeeResponse target = employeeService.deleteEmployee(code);
        ApiResponse response = new ApiResponse(employeeService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }
}