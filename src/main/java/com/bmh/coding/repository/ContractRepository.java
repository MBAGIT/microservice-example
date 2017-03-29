/**
 * 
 */
package com.bmh.coding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bmh.coding.model.Contract;	

/**
 * Interface ContractRepository for CrudRepository<T,ID> from
 * springframework.data.jpa.repository
 * 
 * @author Mohamed
 *
 */
public interface ContractRepository extends JpaRepository<Contract, Long> {


}
