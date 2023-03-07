//package com.ibm.webdev.app.jersey.start.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//@EnableWebMvc
//public class SwaggerConfig {
//	
//	/**
//	 * @ClassName Create Docket
//	 * @Description: Create Docket
//	 * @Date 2023/3/2
//	 * @Version V1.0
//	 **/
//	@Bean
//	public Docket createRestApi() {
//		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//				.apis(RequestHandlerSelectors.basePackage("com.ibm.webdev.app.jersey.resources"))
//				// .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//				.paths(PathSelectors.any()).build();
//	}
//	
//	/**
//	 * @ClassName ApiInfo
//	 * @Description: ApiInfo
//	 * @Date 2023/3/02
//	 * @Version V1.0
//	 **/
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("IBM PIP 2023")
//				.contact(new Contact("chenxb", "https://www.ibm.developer.com/", "xbchenf@cn.ibm.com")).version("1.0.0")
//				.description(" API Document").build();
//	}
//}