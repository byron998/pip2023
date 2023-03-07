package com.ibm.webdev.app.jersey.resources.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


public class HelloDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="Name can't be empty !")
	private String name;
	
	@NotEmpty(message="Email can't be empty !")
	@Email
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
