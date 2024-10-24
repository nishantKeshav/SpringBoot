package com.example.Employee_CRUD.model;

import lombok.Data;
import jakarta.persistence.*;

import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.utils.MartialStatus;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @Column(name = "employee_id" , updatable = false , nullable = false , unique = true)
    private String employeeId;

    @Column(name = "first_name" , nullable = false)
    private String firstName = null;

    @Column(name = "last_name" , nullable = false)
    private String lastName = null;

    @Column(name = "email_id" , unique = true , nullable = false)
    private String emailId = null;

    @Column(name = "phone_number" , nullable = false , unique = true)
    private String phoneNumber = null;

    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAt = null;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;

    @Column(name = "joining_date" , nullable = false)
    private LocalDate joiningDate = null;

    @Column(name = "leaving_date")
    private LocalDate leavingDate = null;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender" , nullable = false)
    private Gender gender = null;

    @Enumerated(EnumType.STRING)
    @Column(name = "martial_status" , nullable = false)
    private MartialStatus martialStatus = null;

    @Column(name = "blood_group" , nullable = false)
    private String bloodGroup = null;

    @Column(name = "department" , nullable = false)
    private String department = null;

    @Column(name = "role" , nullable = false)
    private String role = null;

    public Employee() {
        this.joiningDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.employeeId = UUID.randomUUID().toString();
    }

}
