package com.example.Employee_CRUD.dto.request;

import lombok.Data;

import java.time.LocalDate;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.utils.MartialStatus;

@Data
public class UpdateEmployeeRequest {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private LocalDate joiningDate;
    private LocalDate leavingDate;
    private Gender gender;
    private MartialStatus martialStatus;
    private String bloodGroup;
    private String department;
    private String role;
}
