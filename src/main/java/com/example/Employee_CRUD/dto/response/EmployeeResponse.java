package com.example.Employee_CRUD.dto.response;

import lombok.Data;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.example.Employee_CRUD.model.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
public class EmployeeResponse {

    private int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private String message = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Employee> employeeData = null;

    public static EmployeeResponse response(String message , int statusCode , List<Employee> employeeData) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setMessage(message);
        employeeResponse.setStatusCode(statusCode);
        employeeResponse.setEmployeeData(employeeData);
        return employeeResponse;
    }
}
