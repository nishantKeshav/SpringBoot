package com.example.Employee_CRUD.repository;

import java.util.List;
import java.time.LocalDate;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.model.Employee;
import com.example.Employee_CRUD.utils.MartialStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findEmployeeDetailsByEmailId(String emailId);
    List<Employee> findEmployeeDetailsByEmployeeId(String employeeId);

    @Query("SELECT e FROM Employee e WHERE "
            + "(:role IS NULL OR e.role = :role) AND "
            + "(:gender IS NULL OR e.gender = :gender) AND "
            + "(:bloodGroup IS NULL OR e.bloodGroup = :bloodGroup) AND "
            + "(:department IS NULL OR e.department = :department) AND "
            + "(:martialStatus IS NULL OR e.martialStatus = :martialStatus) AND "
            + "(:startDate IS NULL OR e.joiningDate >= :startDate) AND "
            + "(:endDate IS NULL OR e.joiningDate <= :endDate)")
    List<Employee> findByFilters(
            @Param("bloodGroup") String bloodGroup,
            @Param("gender") Gender gender,
            @Param("department") String department,
            @Param("martialStatus") MartialStatus martialStatus,
            @Param("role") String role,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
