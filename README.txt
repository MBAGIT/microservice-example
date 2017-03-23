I used Spring boot to generate the project whit JPA , H2 as options.

I create two modele customer {id , full name, email}and contract{id,start Date,type,monthly Revenue}.

I mapped by @OneToMany(mappedBy = "customer") set<contract> in customer to retreive all the contracts of the customer.

in the Contract I create customer attribute to mapped my @ManyToOne.


Then I create my JPA repository Interface to get Crud operation from CrudRepository<T,ID> 
ContractRepository
CustomerRepository


With my  test class CustomerRestControllerTest , I start my developpement, I practise TDD Methode
to implement my methodes  :

I add notation @RequestMapping("/customerservice") in my class Controller :

@RequestMapping(method = RequestMethod.PUT, value = "/customer")
ResponseEntity<?> addCustomer(@RequestBody Customer input);

@RequestMapping(method = RequestMethod.PUT, value = "/contract/{customerId}")
ResponseEntity<?> add(@PathVariable Long customerId, @RequestBody Contract input);


@RequestMapping(method = RequestMethod.GET, value = "/customer/{customerId}")
Customer getCustomerInformation(@PathVariable Long customerId);


@RequestMapping(method = RequestMethod.GET, value = "/contract/{customerId}")
ResponseEntity<Double> getSumOfRevenueOfAllContracts(@PathVariable Long customerId);


Swagger 2

// pointing your browser to http://localhost:8080/v2/api-docs



On pointing your browser to http://localhost:8080/swagger-ui.html, 
you will see the generated documentation rendered by Swagger UI





