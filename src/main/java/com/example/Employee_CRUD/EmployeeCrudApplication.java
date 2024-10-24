package com.example.Employee_CRUD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class EmployeeCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeCrudApplication.class, args);
	}

	@GetMapping(name = "/")
	public String applicationChecking() {
		return "Welcome to Employee CRUD Application using Spring Boot!";
	}

}
