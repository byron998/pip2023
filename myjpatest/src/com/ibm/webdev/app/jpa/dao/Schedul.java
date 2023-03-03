package com.ibm.webdev.app.jpa.dao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the schedul database table.
 * 
 */
@Entity
@NamedQuery(name="Schedul.findAll", query="SELECT s FROM Schedul s")
public class Schedul implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private int hall;

	private int price;

	private Timestamp showtime;

	//bi-directional many-to-one association to Movie
	@ManyToOne
	private Movie movie;

	//bi-directional many-to-one association to Cinema
	@ManyToOne
	private Cinema cinema;

	public Schedul() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getHall() {
		return this.hall;
	}

	public void setHall(int hall) {
		this.hall = hall;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Timestamp getShowtime() {
		return this.showtime;
	}

	public void setShowtime(Timestamp showtime) {
		this.showtime = showtime;
	}

	public Movie getMovie() {
		return this.movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Cinema getCinema() {
		return this.cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

}