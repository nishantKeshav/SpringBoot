package com.example.Employee_CRUD.dto.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
public class userProfilePhotoResponse {
    private String message;
    private int statusCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profilePhoto;
}
