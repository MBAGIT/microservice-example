package com.daveo.coding.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.validator.constraints.Email;

import net.minidev.json.annotate.JsonIgnore;

/**
 * Entity Customer class
 * 
 * @author Mohamed
 *
 */
@Entity
public class Customer {

	/**
	 * unique Id customer attribute
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * FullName customer attribute
	 */
	public String fullName;

	/**
	 * email customer attribute
	 */
	@Email
	public String email;

	/**
	 * list of contracts of each customer
	 * 
	 * Each Customer may have no, one, or many Contract entities. This is a 1:N
	 * relationship.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "customer")
	@OrderBy("id")
	public Set<Contract> contracts = new HashSet<>();

	
	/**
	 * Constructor with fields attributes
	 * 
	 * @param fullName
	 * @param email
	 */
	public Customer(String fullName, String email) {

		this.fullName = fullName;
		this.email = email;
	}

	/**
	 * default constructor
	 */
	public Customer() {

	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the customerId
	 */
	public Long getId() {
		return id;
	}

	
}
