package com.example.Employee_CRUD.dto.response;

import lombok.Data;

@Data
public class userLoginResponse {
    private int statusCode;
    private String message;
    private Boolean isRegistered;
}

