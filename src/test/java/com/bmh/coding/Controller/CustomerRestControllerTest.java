/**
 * 
 */
package com.bmh.coding.Controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.bmh.coding.Application;
import com.bmh.coding.model.Contract;
import com.bmh.coding.model.Customer;
import com.bmh.coding.repository.ContractRepository;
import com.bmh.coding.repository.CustomerRepository;

/**
 * 
 * Test class for CustomerService
 * 
 * @author Mohamed
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CustomerRestControllerTest {

	/**
	 * define the contentType with JSON and utf8 charset
	 */
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	/**
	 * use of Class MockMvc Spring MVC test support
	 */
	private MockMvc mockMvc;

	/**
	 * Definition of jackson message converter
	 */
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	/**
	 * Injection of customerRepository
	 */
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Injection of webApplicationContext
	 */
	@Autowired
	private WebApplicationContext webApplicationContext;

	/**
	 * Injection of contractRepository
	 */
	@Autowired
	private ContractRepository contractRepository;

	private Customer customer;

	/**
	 * Initialize the converters for HttpMessageConverter
	 * 
	 * @param converters
	 */
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void initialize() throws Exception {

		// mock webApplicationContext
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		// delete Data
		this.contractRepository.deleteAllInBatch();
		this.customerRepository.deleteAllInBatch();

		// insert New data
		this.customer = customerRepository.save(new Customer("fullName", "email@daveo.com"));
		contractRepository.save(new Contract(new Date(), "A3", 250d, this.customer));
		contractRepository.save(new Contract(new Date(), "B3", 250d, this.customer));
	}

	/**
	 * Test Method To check Not Exist Customer
	 * 
	 * @throws Exception
	 */
	@Test
	public void customerNotFound() throws Exception {
		// send the get request
		mockMvc.perform(get("/customerservice/customer/20").contentType(contentType)).andExpect(status().isNotFound());
	}

	/**
	 * Test Method add a new contract to an existing customer: PUT PUT
	 * /customerservice/contract/$contract_id
	 * 
	 * @throws Exception
	 */
	@Test
	public void createContractToExistingCustomer() throws Exception {
		// create custumer content to send

		String contractJson = json(new Contract(new Date(), "xxxx", 200.00d, customer));

		// send the put request
		mockMvc.perform(put("/customerservice/contract/" + this.customer.getId()).contentType(contentType)
				.content(contractJson)).andExpect(status().isCreated());

	}

	/**
	 * Test Method add a new customer: PUT PUT
	 * /customerservice/customer/$customer_id
	 * 
	 * @throws Exception
	 */
	@Test
	public void createCustomer() throws Exception {
		// create custumer content to send
		String custmerJson = json(new Customer("aab aaf", "abcd@daveo.com"));

		// send the put request
		mockMvc.perform(put("/customerservice/customer").contentType(contentType).content(custmerJson))
				.andExpect(status().isCreated());

	}

	/**
	 * Test Method retrieve all the information about an existing customer,
	 * including their contracts information
	 * 
	 * 
	 * 
	 * GET /customerservice/customer/$customer_id
	 * 
	 * @throws Exception
	 */
	@Test
	public void retrieveAllInformationAboutExistingCustomer() throws Exception {

		mockMvc.perform(get("/customerservice/customer/" + this.customer.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				// "fullName", "email@daveo.com"
				.andExpect(jsonPath("$.fullName", is("fullName"))).andExpect(jsonPath("$.email", is("email@daveo.com")))
				// new Date(), "xxxA", 250d, customer
				// new Date(), "xxxA3", 250d, customer
				.andExpect(jsonPath("$.contracts[0].type", is("A3")))
				.andExpect(jsonPath("$.contracts[0].monthlyRevenue", is(250d)))
				.andExpect(jsonPath("$.contracts[1].type", is("B3")))
				.andExpect(jsonPath("$.contracts[1].monthlyRevenue", is(250d)));

	}

	/**
	 * retrieve the sum of revenues of all contracts from an existing customer:
	 *
	 * GET /customerservice/contract/$customer_id
	 * 
	 * @throws Exception
	 */
	@Test
	public void retrieveSumOfRevenuesOfAllcontractsFromExistingCustumer() throws Exception {

		contractRepository.save(new Contract(new Date(), "C3", 550d, this.customer));

		mockMvc.perform(get("/customerservice/contract/" + this.customer.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", is(1050d)));

	}

	/**
	 * private Method to transform Object to Json string with
	 * mappingJackson2HttpMessageConverter
	 * 
	 * @param o
	 * @return String
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
