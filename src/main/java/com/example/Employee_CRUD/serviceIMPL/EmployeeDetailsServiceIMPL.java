package com.example.Employee_CRUD.serviceIMPL;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.Employee_CRUD.dto.request.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.model.Employee;
import com.example.Employee_CRUD.utils.MartialStatus;
import com.example.Employee_CRUD.utils.EmployeeException;
import com.example.Employee_CRUD.dto.response.EmployeeResponse;
import com.example.Employee_CRUD.repository.EmployeeRepository;
import com.example.Employee_CRUD.service.EmployeeDetailService;

@Service
public class EmployeeDetailsServiceIMPL implements EmployeeDetailService {

    @Autowired
    EmployeeRepository employeeRepository;

    int OK = HttpStatus.OK.value();
    int CREATED = HttpStatus.CREATED.value();
    int NOT_FOUND = HttpStatus.NOT_FOUND.value();
    int FORBIDDEN = HttpStatus.FORBIDDEN.value();
    int INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();

    HttpStatus status = HttpStatus.valueOf(INTERNAL_SERVER_ERROR);
    EmployeeResponse responseBody = null;

    @Override
    public ResponseEntity<EmployeeResponse> addEmployeeDetails(AddEmployeeRequest addEmployeeRequestData) {
        try {
            String firstName = addEmployeeRequestData.getFirstName();
            String lastName = addEmployeeRequestData.getLastName();
            String emailId = addEmployeeRequestData.getEmailId();
            String phoneNumber = addEmployeeRequestData.getPhoneNumber();
            Gender gender = addEmployeeRequestData.getGender();
            MartialStatus martialStatus = addEmployeeRequestData.getMartialStatus();
            String department = addEmployeeRequestData.getDepartment();
            String bloodGroup = addEmployeeRequestData.getBloodGroup();
            String role = addEmployeeRequestData.getRole();

            if (firstName == null || lastName == null || emailId == null || phoneNumber == null || bloodGroup == null || department == null || role == null || martialStatus == null || gender == null || firstName.isBlank() || lastName.isBlank() || emailId.isBlank() || phoneNumber.isBlank() || bloodGroup.isBlank() || department.isBlank() || role.isBlank()) {
                throw new EmployeeException("Employee details Inadequate!" , HttpStatus.FORBIDDEN);
            }

            List<Employee> employee = employeeRepository.findEmployeeDetailsByEmailId(emailId);
            if (!employee.isEmpty()) {
                throw new EmployeeException("Employee already exists!" , HttpStatus.FORBIDDEN);
            }

            Employee newEmployee = new Employee();

            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setEmailId(emailId);
            newEmployee.setPhoneNumber(phoneNumber);
            newEmployee.setDepartment(department);
            newEmployee.setBloodGroup(bloodGroup);
            newEmployee.setRole(role);
            newEmployee.setGender(Gender.valueOf(String.valueOf(gender)));
            newEmployee.setMartialStatus(MartialStatus.valueOf(String.valueOf(martialStatus)));

            employeeRepository.save(newEmployee);

            responseBody = EmployeeResponse.response("Employee Added Successfully!" , CREATED , List.of(newEmployee));
            status = HttpStatus.CREATED;

        } catch (EmployeeException e) {
            responseBody = EmployeeResponse.response(e.getMessage() , e.getStatus().value() , null);
            status = e.getStatus();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseBody = EmployeeResponse.response("Internal Server Error!" , INTERNAL_SERVER_ERROR , null);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseBody , status);
    }

    @Override
    public ResponseEntity<EmployeeResponse> getEmployeesDetails(GetEmployeesRequest getEmployeesRequest) {
        try {
            String bloodGroup = getEmployeesRequest.getBloodGroup();
            Gender gender = getEmployeesRequest.getGender();
            String department = getEmployeesRequest.getDepartment();
            MartialStatus martialStatus = getEmployeesRequest.getMartialStatus();
            String role = getEmployeesRequest.getRole();
            LocalDate startDate = getEmployeesRequest.getStartDate();
            LocalDate endDate = getEmployeesRequest.getEndDate();

            List<Employee> employees = employeeRepository.findByFilters(
                    bloodGroup, gender, department, martialStatus, role, startDate, endDate
            );
            responseBody = EmployeeResponse.response("Employee data fetched successfully!", OK, employees);
            status = HttpStatus.OK;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseBody = EmployeeResponse.response("Internal server error!", INTERNAL_SERVER_ERROR, null);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseBody, status);
    }

    @Override
    public ResponseEntity<EmployeeResponse> deleteEmployeeDetails(DeleteEmployeeRequest deleteEmployeeRequestData) {
        try {
            String emailId = deleteEmployeeRequestData.getEmailId();
            if (emailId == null || emailId.isBlank()) {
                throw new EmployeeException("Employee details inadequate!" , HttpStatus.FORBIDDEN);
            }
            List<Employee> employeeDetails = employeeRepository.findEmployeeDetailsByEmailId(emailId);

            if (employeeDetails.isEmpty()) {
                throw new EmployeeException("Employee does not exists!" , HttpStatus.NOT_FOUND);
            }

            Employee employee = employeeDetails.get(0);
            employeeRepository.delete(employee);

            responseBody = EmployeeResponse.response("Employee details deleted successfully!" , OK , null);
            status = HttpStatus.OK;

        } catch (EmployeeException e) {
            responseBody = EmployeeResponse.response(e.getMessage() , e.getStatus().value() , null);
            status = e.getStatus();
        } catch (Exception e) {
            responseBody = EmployeeResponse.response("Internal server error!" , INTERNAL_SERVER_ERROR , null);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseBody , status);
    }

    @Override
    public ResponseEntity<EmployeeResponse> updateEmployeeDetails(UpdateEmployeeRequest updateEmployeeRequest) {
        try {

            String employeeId = updateEmployeeRequest.getEmployeeId();

            if (employeeId == null || employeeId.isBlank()) {
                throw new EmployeeException("Employee details inadequate!" , HttpStatus.FORBIDDEN);
            }

            List<Employee> employee = employeeRepository.findEmployeeDetailsByEmployeeId(employeeId);
            if (employee.isEmpty()) {
                throw new EmployeeException("Employee does not exists!" ,HttpStatus.NOT_FOUND);
            }

            boolean role = updateEmployeeRequest.getRole() != null;
            boolean gender = updateEmployeeRequest.getGender() != null;
            boolean emailId = updateEmployeeRequest.getEmailId() != null;
            boolean lastName = updateEmployeeRequest.getLastName() != null;
            boolean firstName = updateEmployeeRequest.getFirstName() != null;
            boolean bloodGroup = updateEmployeeRequest.getBloodGroup() != null;
            boolean department = updateEmployeeRequest.getDepartment() != null;
            boolean phoneNumber = updateEmployeeRequest.getPhoneNumber() != null;
            boolean joiningDate = updateEmployeeRequest.getJoiningDate() != null;
            boolean leavingDate = updateEmployeeRequest.getLeavingDate() != null;
            boolean martialStatus = updateEmployeeRequest.getMartialStatus() != null;


            if (!role && !gender && !emailId && !lastName && !firstName && !bloodGroup && !department && !phoneNumber && !joiningDate && !leavingDate && !martialStatus ) {
                System.out.println("Error 1");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (role && updateEmployeeRequest.getRole().isBlank()) {
                System.out.println("Error 2");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (emailId && updateEmployeeRequest.getEmailId().isBlank()) {
                System.out.println("Error 3");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (lastName && updateEmployeeRequest.getLastName().isBlank()) {
                System.out.println("Error 4");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (firstName && updateEmployeeRequest.getFirstName().isBlank()) {
                System.out.println("Error 5");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (bloodGroup && updateEmployeeRequest.getBloodGroup().isBlank()) {
                System.out.println("Error 6");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (department && updateEmployeeRequest.getDepartment().isBlank()) {
                System.out.println("Error 7");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (phoneNumber && updateEmployeeRequest.getPhoneNumber().isBlank()) {
                System.out.println("Error 8");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (joiningDate && updateEmployeeRequest.getJoiningDate().isBefore(LocalDate.of(1940, 1, 1))) {
                System.out.println("Error 9");
                throw new EmployeeException("Date of Joining cannot be before 1940/01/01", HttpStatus.FORBIDDEN);
            }

            if (leavingDate && updateEmployeeRequest.getLeavingDate().isAfter(LocalDate.of(2100 , 1 , 1))) {
                System.out.println("Error 10");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (gender && updateEmployeeRequest.getGender().toString().isBlank() && !updateEmployeeRequest.getGender().toString().equals("MALE") && !updateEmployeeRequest.getGender().toString().equals("FEMALE") && !updateEmployeeRequest.getGender().toString().equals("OTHER")) {
                System.out.println("Error 11");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            if (martialStatus && updateEmployeeRequest.getMartialStatus().toString().isBlank() && !updateEmployeeRequest.getMartialStatus().toString().equals("MARRIED") && !updateEmployeeRequest.getMartialStatus().toString().equals("UNMARRIED")) {
                System.out.println("Error 12");
                throw new EmployeeException("Field for update is missing, or it cannot be empty or zero!" , HttpStatus.FORBIDDEN);
            }

            Employee employeeDetails = employee.get(0);

            if (role) {
                employeeDetails.setRole(updateEmployeeRequest.getRole());
            }

            if (emailId) {
                employeeDetails.setEmailId(updateEmployeeRequest.getEmailId());
            }

            if (firstName) {
                employeeDetails.setFirstName(updateEmployeeRequest.getFirstName());
            }

            if (lastName) {
                employeeDetails.setLastName(updateEmployeeRequest.getLastName());
            }

            if (bloodGroup) {
                employeeDetails.setBloodGroup(updateEmployeeRequest.getBloodGroup());
            }

            if (department) {
                employeeDetails.setDepartment(updateEmployeeRequest.getDepartment());
            }

            if (phoneNumber) {
                employeeDetails.setPhoneNumber(updateEmployeeRequest.getPhoneNumber());
            }

            if (joiningDate) {
                employeeDetails.setJoiningDate(updateEmployeeRequest.getJoiningDate());
            }

            if (leavingDate) {
                employeeDetails.setLeavingDate(updateEmployeeRequest.getLeavingDate());
            }

            if (gender) {
                employeeDetails.setGender(updateEmployeeRequest.getGender());
            }

            if (martialStatus) {
                employeeDetails.setMartialStatus(updateEmployeeRequest.getMartialStatus());
            }

            employeeDetails.setUpdatedAt(LocalDateTime.now());

            Employee updatedEmployeeDetails = employeeRepository.save(employeeDetails);
            responseBody = EmployeeResponse.response("Employee details updated successfully!" , OK , List.of(updatedEmployeeDetails));
            status = HttpStatus.OK;
        } catch (EmployeeException e) {
            responseBody = EmployeeResponse.response(e.getMessage() , e.getStatus().value() , null);
            status = e.getStatus();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseBody = EmployeeResponse.response("Internal sever error!" , INTERNAL_SERVER_ERROR , null);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseBody , status);
    }

    @Override
    public ResponseEntity<EmployeeResponse> getSingleEmployeeDetails(GetSingleEmployeeRequest getSingleEmployeeRequestData) {
        try {
            String emailId = getSingleEmployeeRequestData.getEmailId();
            if (emailId == null || emailId.isBlank()) {
                throw new EmployeeException("Employee details inadequate!" , HttpStatus.FORBIDDEN);
            }
            List<Employee> employeeDetails = employeeRepository.findEmployeeDetailsByEmailId(emailId);

            if (employeeDetails.isEmpty()) {
                throw new EmployeeException("Employee does not exists!" , HttpStatus.NOT_FOUND);
            }
            responseBody = EmployeeResponse.response("Employee details fetched successfully!" , OK , employeeDetails);
            status = HttpStatus.OK;
        } catch (EmployeeException e) {
            responseBody = EmployeeResponse.response(e.getMessage() , e.getStatus().value() , null);
            status = e.getStatus();
        } catch (Exception e) {
            responseBody = EmployeeResponse.response("Internal server error!" , INTERNAL_SERVER_ERROR , null);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseBody , status);
    }

}
