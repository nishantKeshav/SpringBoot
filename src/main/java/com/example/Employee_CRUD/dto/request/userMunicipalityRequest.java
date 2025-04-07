package com.example.Employee_CRUD.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class userMunicipalityRequest {
    @NotNull(message = "Block name is required")
    private String block;
}
