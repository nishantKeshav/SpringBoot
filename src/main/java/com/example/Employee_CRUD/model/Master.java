package com.example.Employee_CRUD.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "master")
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false , unique = true)
    private Long id;

    @Column(name = "block" , nullable = false , updatable = false)
    private String block;

    @Column(name = "grampanchayat" , nullable = false , updatable = false)
    private String grampanchayat;

    @Column(name = "village" , nullable = false , updatable = false)
    private String village;
}
