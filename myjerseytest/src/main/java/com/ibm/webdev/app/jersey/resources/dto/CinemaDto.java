package com.ibm.webdev.app.jersey.resources.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;

public class CinemaDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "{cinema.id.isempty.error}", groups = { Validgroup.update.class })
	private String id;

	@NotBlank(message = "{cinema.name.isblank.error}", groups = { Validgroup.insert.class, Validgroup.update.class,
			Default.class })
	private String name;

	@NotBlank(message = "{cinema.city.isblank.error}", groups = { Validgroup.insert.class, Validgroup.update.class,
			Default.class })
	private String city;

	@NotBlank(message = "{cinema.address.isblank.error}", groups = { Validgroup.insert.class, Validgroup.update.class,
			Default.class })
	private String address;

	private int hallcount;

	public CinemaDto() {
	}

	public CinemaDto(String id, String name, String city, String address, int hallcount) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.address = address;
		this.hallcount = hallcount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getHallcount() {
		return hallcount;
	}

	public void setHallcount(int hallcount) {
		this.hallcount = hallcount;
	}

}
