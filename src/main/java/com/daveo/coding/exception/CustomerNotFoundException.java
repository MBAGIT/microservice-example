/**
 * 
 */
package com.daveo.coding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CustomerNotFoundException throw 
 * in the case of Customer Not exist.
 * @author Mohamed
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * use of constructor to specify the message
	 * @param customerId
	 */
	public CustomerNotFoundException(Long customerId) {
		super("could not find customer '" + customerId + "'.");
	}

}
