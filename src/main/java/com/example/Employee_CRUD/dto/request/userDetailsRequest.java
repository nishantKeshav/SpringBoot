package com.example.Employee_CRUD.dto.request;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Data
public class userDetailsRequest {
    @Valid
    @NotEmpty(message = "Mobile number is required")
    @NotNull(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    private String mobileNumber;
}
