package com.ibm.webdev.app.jpa.dao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the movie database table.
 * 
 */
@Entity
@NamedQuery(name="Movie.findAll", query="SELECT m FROM Movie m")
@Table(name="tb_movie")
public class Movie implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String actor;

	private String director;

	private int mins;

	private String name;

	private String placard;

	private int year;

	public Movie() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActor() {
		return this.actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getDirector() {
		return this.director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getMins() {
		return this.mins;
	}

	public void setMins(int mins) {
		this.mins = mins;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlacard() {
		return this.placard;
	}

	public void setPlacard(String placard) {
		this.placard = placard;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}