package com.ibm.webdev.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan           (basePackages = {"com.ibm.webdev.app.jpa.dao"})
@EnableJpaRepositories(basePackages = {"com.ibm.webdev.app.jpa.repository"})
@ServletComponentScan (basePackages = {"com.ibm.webdev.app.jersey.start.filter"})
public class RestApp {
	public static void main(String[] args) {
        SpringApplication.run(RestApp.class, args);
    }
}
