/**
 * 
 */
package com.bmh.coding.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bmh.coding.exception.CustomerNotFoundException;
import com.bmh.coding.model.Contract;
import com.bmh.coding.model.Customer;
import com.bmh.coding.repository.ContractRepository;
import com.bmh.coding.repository.CustomerRepository;

/**
 * @author Mohamed
 *
 */
@RestController
@RequestMapping("/customerservice")
public class CustomerRestController {

	/**
	 * Repository to use
	 */
	private final ContractRepository contractRepository;

	private final CustomerRepository customerRepository;

	/**
	 * Constructor with fields
	 * 
	 * @param contractRepository
	 * @param customerRepository
	 */
	@Autowired
	public CustomerRestController(ContractRepository contractRepository, CustomerRepository customerRepository) {
		this.contractRepository = contractRepository;
		this.customerRepository = customerRepository;
	}

	/**
	 * Method to add New Customer
	 * 
	 * @param input
	 *            Customer to save
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/customer")
	ResponseEntity<?> addCustomer(@RequestBody Customer input) {
		try {

			// customer to save
			Customer savedCustomer = this.customerRepository.save(new Customer(input.fullName, input.email));

			// construct location to send the new entity
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(savedCustomer).toUri();

			return ResponseEntity.created(location).build();

		} catch (Exception e) {

			return ResponseEntity.noContent().build();

		}

	}

	/**
	 * Method to add New Contract
	 * 
	 * @param customerId
	 * @param input
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/contract/{customerId}")
	ResponseEntity<?> add(@PathVariable Long customerId, @RequestBody Contract input) {
		this.validateCustomer(customerId);

		return this.customerRepository.findById(customerId).map(customer -> {
			Contract result = contractRepository
					.save(new Contract(input.startDate, input.type, input.monthlyRevenue, customer));

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();

			return ResponseEntity.created(location).build();
		}).orElse(ResponseEntity.noContent().build());

	}

	/**
	 * Method to show All Informations of the Customer
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/customer/{customerId}")
	Customer getCustomerInformation(@PathVariable Long customerId) {
		return this.customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException(customerId));

	}

	/**
	 * Method retrieve the sum of revenues of all contracts from an existing
	 * customer
	 * 
	 * @param customerId
	 * @return {@link ResponseEntity}
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/contract/{customerId}")
	ResponseEntity<Double> getSumOfRevenueOfAllContracts(@PathVariable Long customerId) {
		this.validateCustomer(customerId);

		return new ResponseEntity<Double>(this.customerRepository.findOne(customerId).contracts.stream()
				.mapToDouble(contract -> contract.monthlyRevenue).sum(), HttpStatus.OK);

	}

	/**
	 * Method to validate Customer Throw Exception if Not found
	 * 
	 * @param customerId
	 */
	private void validateCustomer(Long customerId) {
		this.customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));

	}

}
