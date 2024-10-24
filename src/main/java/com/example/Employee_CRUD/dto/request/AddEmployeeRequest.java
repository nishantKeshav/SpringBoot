package com.example.Employee_CRUD.dto.request;

import lombok.Data;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.utils.MartialStatus;

@Data
public class AddEmployeeRequest {
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private Gender gender;
    private MartialStatus martialStatus;
    private String bloodGroup;
    private String department;
    private String role;
}
