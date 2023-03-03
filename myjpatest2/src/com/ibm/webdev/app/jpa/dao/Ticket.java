package com.ibm.webdev.app.jpa.dao;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ticket database table.
 * 
 */
@Entity
@NamedQuery(name="Ticket.findAll", query="SELECT t FROM Ticket t")
@Table(name="tb_ticket")
public class Ticket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Timestamp bookingtime;

	private String phone;

	//bi-directional many-to-one association to Schedul
	@ManyToOne
	private Schedul schedul;

	public Ticket() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getBookingtime() {
		return this.bookingtime;
	}

	public void setBookingtime(Timestamp bookingtime) {
		this.bookingtime = bookingtime;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Schedul getSchedul() {
		return this.schedul;
	}

	public void setSchedul(Schedul schedul) {
		this.schedul = schedul;
	}

}