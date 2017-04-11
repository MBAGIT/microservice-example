package com.bmh.coding;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.bmh.coding.model.Customer;
import com.bmh.coding.repository.CustomerRepository;

@EnableEurekaClient
@SpringBootApplication
public class Application {
	
	
	@Bean
	CommandLineRunner demo (CustomerRepository customerRepository){
		return args -> {
			
			Stream.of("cust1,test@gmail.com","cust2,test2@gmail.com","cust3,test3@gmail.com","cust4,test4@gmail.com")
			.map(x -> x.split(","))
			.map(entity -> new Customer(entity[0], entity[1]))
			.forEach(cust -> customerRepository.save(cust));
			
		};
		
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
