package com.example.Employee_CRUD.dto.request;

import lombok.Data;

import java.time.LocalDate;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.utils.MartialStatus;


@Data
public class GetEmployeesRequest {
    private String bloodGroup;
    private Gender gender;
    private String department;
    private MartialStatus martialStatus;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
}
