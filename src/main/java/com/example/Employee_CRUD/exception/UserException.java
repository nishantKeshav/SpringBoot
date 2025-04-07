package com.example.Employee_CRUD.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Throwable{

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public UserException(String message, Throwable cause) {

        super(message, cause);
    }

    public UserException(String  message, HttpStatus status) {
        super((message == null || message.isEmpty()) ? "Internal Server Error" : message);
        this.status = status;
    }
}
