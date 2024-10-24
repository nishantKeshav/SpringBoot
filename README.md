
# Employee Management System - Backend API

## Overview

This is a Spring Boot backend application for managing an employee database. The application provides a RESTful API that allows users to perform CRUD operations on employee data, including adding new employees, updating employee details, retrieving employee data, filtering employees by specific criteria, and deleting employee records.

## Features

- **Add Employee:** Add a new employee with various details such as name, gender, department, etc.
- **View All Employees:** Retrieve a list of all employees.
- **View Single Employee:** Retrieve detailed information of a specific employee by their ID.
- **Update Employee:** Modify details of an existing employee.
- **Delete Employee:** Remove an employee from the system.
- **Filter Employees:** Dynamically filter employees by attributes such as gender, blood group, department, marital status, and date of joining range.

## Tech Stack

- **Backend Framework:** Spring Boot
- **Database:** MySQL (or any relational database supported by Spring JPA)
- **Java Version:** 11+ (Compatible with Java 8 as well)

## Prerequisites

- Java 11+
- Maven or Gradle
- MySQL or any other relational database

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/employee-management-system.git
cd employee-management-system
```

### 2. Configure the Database

Update the `application.properties` (or `application.yml`) with your database connection details.

For example, in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build the Application

If you are using Maven, build the project using:

```bash
mvn clean install
```

For Gradle users:

```bash
./gradlew build
```

### 4. Run the Application

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

Or if you're using Gradle:

```bash
./gradlew bootRun
```

The application will start on [http://localhost:8080](http://localhost:8080).

## API Endpoints

### 1. **Get All Employees**
   - **Endpoint:** `/api/employees`
   - **Method:** GET
   - **Description:** Retrieve a list of all employees.
   - **Response Example:**
   ```json
   [
     {
       "id": 1,
       "name": "John Doe",
       "gender": "Male",
       "department": "Engineering",
       "maritalStatus": "Single"
     },
     {
       "id": 2,
       "name": "Jane Smith",
       "gender": "Female",
       "department": "HR",
       "maritalStatus": "Married"
     }
   ]
   ```

### 2. **Get Single Employee by ID**
   - **Endpoint:** `/api/employees/{id}`
   - **Method:** GET
   - **Description:** Retrieve details of a single employee by their ID.
   - **Response Example:**
   ```json
   {
     "id": 1,
     "name": "John Doe",
     "gender": "Male",
     "department": "Engineering",
     "bloodGroup": "O+",
     "maritalStatus": "Single",
     "joiningDate": "2020-01-15"
   }
   ```

### 3. **Add a New Employee**
   - **Endpoint:** `/api/employees`
   - **Method:** POST
   - **Description:** Add a new employee to the database.
   - **Request Body Example:**
   ```json
   {
     "name": "John Doe",
     "gender": "Male",
     "department": "Engineering",
     "bloodGroup": "O+",
     "maritalStatus": "Single",
     "joiningDate": "2020-01-15"
   }
   ```
   - **Response Example:**
   ```json
   {
     "id": 3,
     "name": "John Doe",
     "message": "Employee added successfully"
   }
   ```

### 4. **Update Employee Details**
   - **Endpoint:** `/api/employees/{id}`
   - **Method:** PUT
   - **Description:** Update details of an existing employee.
   - **Request Body Example:**
   ```json
   {
     "name": "John Doe",
     "gender": "Male",
     "department": "Engineering",
     "bloodGroup": "O+",
     "maritalStatus": "Married"
   }
   ```
   - **Response Example:**
   ```json
   {
     "id": 1,
     "message": "Employee updated successfully"
   }
   ```

### 5. **Delete Employee**
   - **Endpoint:** `/api/employees/{id}`
   - **Method:** DELETE
   - **Description:** Delete an employee by their ID.
   - **Response Example:**
   ```json
   {
     "message": "Employee deleted successfully"
   }
   ```

### 6. **Filter Employees**
   - **Endpoint:** `/api/employees/filter`
   - **Method:** GET
   - **Query Parameters:** 
     - `gender`: Filter by gender (e.g., `Male`, `Female`)
     - `department`: Filter by department (e.g., `HR`, `Engineering`)
     - `maritalStatus`: Filter by marital status (e.g., `Single`, `Married`)
     - `bloodGroup`: Filter by blood group (e.g., `O+`, `A-`)
     - `startDate`: Start date for joining date range
     - `endDate`: End date for joining date range
   - **Description:** Dynamically filter employees based on one or more criteria.
   - **Example Request:**
     `/api/employees/filter?gender=Male&department=Engineering&maritalStatus=Single`
   - **Response Example:**
   ```json
   [
     {
       "id": 1,
       "name": "John Doe",
       "gender": "Male",
       "department": "Engineering",
       "maritalStatus": "Single"
     }
   ]
   ```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## Contact

For any queries or support, please contact:

- **Nishant Keshav**
- Email: nishant@example.com
