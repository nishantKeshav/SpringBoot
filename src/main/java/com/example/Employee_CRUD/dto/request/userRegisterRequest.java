package com.example.Employee_CRUD.dto.request;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class userRegisterRequest {

    @NotBlank(message = "District cannot be empty")
    @NotNull(message = "District cannot be null")
    private String district;

    @NotBlank(message = "Block cannot be empty")
    @NotNull(message = "Block cannot be null")
    private String block;

    @NotNull(message = "Municipality cannot be null")
    @NotBlank(message = "Municipality cannot be empty")
    private String municipality;

    @NotNull(message = "Village cannot be null")
    @NotBlank(message = "Village cannot be empty")
    private String village;

    @NotNull(message = "Ward cannot be null")
    @NotBlank(message = "Ward cannot be empty")
    private String ward;

    @NotBlank(message = "Full Name cannot be empty")
    @NotNull(message = "Full Name cannot be null")
    private String fullName;

    @NotBlank(message = "Father Name cannot be empty")
    @NotNull(message = "Father Name cannot be null")
    private String fatherName;

    @NotBlank(message = "Gender cannot be empty")
    @NotNull(message = "Gender cannot be null")
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Gender must be MALE, FEMALE or OTHER")
    private String gender;

    @NotNull(message = "Date of Birth cannot be null")
    @NotBlank(message = "Date of Birth cannot be empty")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of Birth must be in the format yyyy-MM-dd")
    private String dateOfBirth;

    //    @NotBlank(message = "Blood Group cannot be empty")
    @NotNull(message = "Blood Group cannot be null")
    private String bloodGroup;

    @NotNull(message = "Voter ID cannot be null")
    private String voterId;

    @NotBlank(message = "Mobile Number cannot be empty")
    @NotNull(message = "Mobile Number cannot be null")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Mobile Number")
    private String mobileNumber;

    //    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid Email id")
    @NotNull(message = "Email cannot be null")
    private String email;

    //    @NotBlank(message = "Whatsapp Mobile Number cannot be empty")
//    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Whatsapp Mobile Number")
    @NotNull(message = "Whatsapp Mobile Number cannot be null")
    private String whatsappNumber;

    @NotNull(message = "Instagram Link cannot be null")
    private String instagramLink;

    @NotNull(message = "Facebook Link cannot be null")
    private String facebookLink;

    @NotNull(message = "Referred By Mobile Number cannot be null")
    private String referredByMobileNumber;
}
