package com.example.Employee_CRUD.dto.response;

import lombok.Data;

@Data
public class userDetailsResponse {
    private int statusCode;
    private String message;
    private userData data;
}
