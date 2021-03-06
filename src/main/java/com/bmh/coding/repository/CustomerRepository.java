/**
 * 
 */
package com.bmh.coding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmh.coding.model.Customer;



/**
 * * Interface CustomerRepository for CrudRepository<T,ID> 
 * from springframework.data.jpa.repository
 * 
 * @author Mohamed
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findById(Long customerId);
}
