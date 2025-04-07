package com.example.Employee_CRUD.dto.response;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
public class userAddressResponse {
    private String message;
    private int statusCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> data;
}
