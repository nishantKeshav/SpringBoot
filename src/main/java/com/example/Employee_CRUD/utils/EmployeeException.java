package com.example.Employee_CRUD.utils;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class EmployeeException extends Exception {

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public EmployeeException(String message , HttpStatus status) {
        super(message);
        this.status = status;
    }
}
