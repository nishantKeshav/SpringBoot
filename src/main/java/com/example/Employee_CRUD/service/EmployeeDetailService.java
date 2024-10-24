package com.example.Employee_CRUD.service;

import com.example.Employee_CRUD.dto.request.*;
import org.springframework.http.ResponseEntity;

import com.example.Employee_CRUD.dto.response.EmployeeResponse;

public interface EmployeeDetailService {

    ResponseEntity<EmployeeResponse> addEmployeeDetails(AddEmployeeRequest addEmployeeRequestData);
    ResponseEntity<EmployeeResponse> getEmployeesDetails(GetEmployeesRequest getEmployeesRequestData);
    ResponseEntity<EmployeeResponse> deleteEmployeeDetails(DeleteEmployeeRequest deleteEmployeeRequestData);
    ResponseEntity<EmployeeResponse> updateEmployeeDetails(UpdateEmployeeRequest updateEmployeeRequestData);
    ResponseEntity<EmployeeResponse> getSingleEmployeeDetails(GetSingleEmployeeRequest getSingleEmployeeRequestData);
}
