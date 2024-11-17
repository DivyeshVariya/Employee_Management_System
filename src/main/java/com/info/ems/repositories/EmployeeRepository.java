package com.info.ems.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.info.ems.models.Employee;

/**
 * Repository interface for managing Employee entities.
 * Extends JpaRepository to provide CRUD operations and additional query methods.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Retrieves an Employee entity based on the provided email address.
     *
     * @param email the email address of the employee to find
     * @return an Optional containing the Employee if found, or empty if not found
     */
    Optional<Employee> findByEmail(String email);
}
