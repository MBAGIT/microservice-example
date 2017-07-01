/**
 * 
 */
package com.bmh.coding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Mohamed
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket customerServiceApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.bmh.coding.controller"))
				.paths(regex("/customerservice.*")).build().apiInfo(apiInfo());

	}

	/**
	 * Api Info description using swagger UI
	 * @return ApiInfo
	 */
	private ApiInfo apiInfo() {
		// Api Info description
		// metadata
		return new ApiInfo("Customer Service Demo", "Simple Microservice Demo using Spring Boot", "1.0",
				"Daveo UsesCase", new Contact("Babchia ", "MBAGIT@github.com", "mbabchia.com"),
				"https://github.com/MBAGIT", "@BMH");
	}

	/**
	 * test if the url match "/customerservice.*"
	 * 
	 * @param urlToMatch
	 * @return Predicate<String>
	 */
	private Predicate<String> regex(String urlToMatch) {
		// init predicate function
		Predicate<String> isUrlMatch = (s) -> s.matches(urlToMatch);
		// apply test
		isUrlMatch.apply(urlToMatch);

		return isUrlMatch;

	}

}
