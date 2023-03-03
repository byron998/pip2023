package com.ibm.webdev.app.jpa.dao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the cenema database table.
 * 
 */
@Entity
@NamedQuery(name="Cinema.findAll", query="SELECT c FROM Cinema c")
public class Cinema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String address;

	private String city;

	private int hallcount;

	private String name;

	public Cinema() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getHallcount() {
		return this.hallcount;
	}

	public void setHallcount(int hallcount) {
		this.hallcount = hallcount;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}