package com.example.Employee_CRUD.controller;

import com.example.Employee_CRUD.dto.request.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.utils.MartialStatus;
import com.example.Employee_CRUD.service.EmployeeDetailService;
import com.example.Employee_CRUD.dto.response.EmployeeResponse;

import java.time.LocalDate;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeDetailService employeeDetailService;

    @PostMapping("/employee/createEmployee")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody AddEmployeeRequest addEmployeeRequestData) {
        return employeeDetailService.addEmployeeDetails(addEmployeeRequestData);
    }

    @GetMapping("/employee/getEmployees")
    public ResponseEntity<EmployeeResponse> getEmployees(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) MartialStatus martialStatus,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        GetEmployeesRequest getEmployeesRequest = new GetEmployeesRequest();

        getEmployeesRequest.setBloodGroup(bloodGroup);
        getEmployeesRequest.setGender(gender);
        getEmployeesRequest.setDepartment(department);
        getEmployeesRequest.setMartialStatus(martialStatus);
        getEmployeesRequest.setRole(role);
        getEmployeesRequest.setStartDate(startDate);
        getEmployeesRequest.setEndDate(endDate);

        return employeeDetailService.getEmployeesDetails(getEmployeesRequest);

    }

    @PostMapping("/employee/deleteEmployee")
    public ResponseEntity<EmployeeResponse> deleteEmployee(@RequestBody DeleteEmployeeRequest deleteEmployeeRequestData) {
        return employeeDetailService.deleteEmployeeDetails(deleteEmployeeRequestData);
    }

    @PostMapping("/employee/updateEmployee")
    public ResponseEntity<EmployeeResponse> updateEmployee(@RequestBody UpdateEmployeeRequest updateEmployeeRequestData) {
        return employeeDetailService.updateEmployeeDetails(updateEmployeeRequestData);
    }

    @GetMapping("/employee/getSingleEmployeeDetails/{emailId}")
    public ResponseEntity<EmployeeResponse> getSingleEmployee(@PathVariable("emailId") String emailId) {
        GetSingleEmployeeRequest request = new GetSingleEmployeeRequest();
        request.setEmailId(emailId);
        return employeeDetailService.getSingleEmployeeDetails(request);
    }
}
