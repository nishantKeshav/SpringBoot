package com.example.Employee_CRUD.dto.response;

import lombok.Data;

@Data
public class userRegisterResponse {
    private String message;
    private int statusCode;
    private Boolean isRegistered = true;
}
