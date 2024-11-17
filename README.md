# Employee Management System (EMS)

## Overview
The Employee Management System (EMS) is a RESTful web application built using **Spring Boot** that allows managing employee records. It provides endpoints to create, read, update, and delete employee information, following the standard CRUD operations.

This system is designed to manage employees efficiently with features like employee creation, retrieval, updating, and deletion and also keep track of system trasaction by audit logs. It uses `Spring Boot` for REST APIs, `Spring Data JPA` for database interactions, and `JUnit` for testing.

## Features

- **Create Employee**: Allows creating new employee records with details such as name and position.
- **Get Employee**: Allows retrieving the details of an employee by their ID.
- **Update Employee**: Allows updating the details of an existing employee.
- **Delete Employee**: Allows deleting an employee by their ID.
  
## Technologies Used

- **Spring Boot**: Framework for building the REST API.
- **Spring Data JPA**: For interacting with the database.
- **JUnit 5**: For writing unit tests.
- **Mockito**: For mocking the service layer in the unit tests.
- **MySQL Database** (Optional): In-memory database for testing purposes.

## Getting Started

### Prerequisites

To run this project locally, you need the following software:

- **Java 11+**
- **Maven** or **Gradle** (for dependency management)
- **IDE** (e.g., IntelliJ IDEA, Eclipse)

### Clone the Repository

Clone this repository to your local machine using the following command:

```bash
git clone https://github.com/your-username/employee-management-system.git
