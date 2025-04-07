package com.example.Employee_CRUD.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.utils.BloodGroup;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false, unique = true)
    private Long userId;

    @Column(name = "district" , nullable = false , updatable = false)
    private String district;

    @Column(name = "block" , nullable = false  , updatable = false)
    private String block;

    @Column(name = "municipality" , nullable = false  , updatable = false)
    private String municipality;

    @Column(name = "village" , nullable = false  , updatable = false)
    private String village;

    @Column(name = "ward" , nullable = false  , updatable = false)
    private String ward;

    @Column(name = "full_name", nullable = false , updatable = false)
    private String fullName;

    @Column(name = "father_name", nullable = false, updatable = false)
    private String fatherName;

    @Column(name = "date_of_birth", nullable = false, updatable = false)
    private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, updatable = false)
    private Gender gender;

    @Column(name = "mobile_number", updatable = false, nullable = false, unique = true)
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = true, updatable = false)
    private BloodGroup bloodGroup;

    @Column(name = "voter_id", nullable = true, updatable = false, unique = true)
    private String voterId;

    @Column(name = "email_id" , nullable = true , updatable = false , unique = true)
    private String emailId;

    @Column(name = "whatsapp_number" , nullable = true , updatable = false , unique = true)
    private String whatsappNumber;

    @Column(name = "facebook_link", nullable = true, updatable = false , unique = true)
    private String facebookLink;

    @Column(name = "instagram_link", nullable = true, updatable = false, unique = true)
    private String instagramLink;

    @Column(name = "referred_by_mobile_number" , nullable = true , updatable = false)
    private String referredByMobileNumber;

    @Column(name = "photo" , nullable = false , updatable = true)
    private Boolean photo = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}