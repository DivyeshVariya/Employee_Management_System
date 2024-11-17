package com.info.ems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.info.ems.models.AuditLog;
/**
 * Repository interface for managing AuditLog entities.
 * Extends JpaRepository to provide CRUD operations and additional query methods.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

}
