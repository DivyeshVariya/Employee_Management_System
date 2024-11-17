package com.info.ems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.info.ems.models.Address;
/**
 * Repository interface for managing Address entities.
 * Extends JpaRepository to provide CRUD operations and additional query methods.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}
