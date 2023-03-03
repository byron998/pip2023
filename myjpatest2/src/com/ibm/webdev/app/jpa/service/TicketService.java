package com.ibm.webdev.app.jpa.service;

import java.util.List;

import com.ibm.webdev.app.jpa.dao.Ticket;

public interface TicketService {
	public List<Ticket> findAllTickets();
	public Ticket findTicketById(Long id);
	public boolean updateTicketById(Ticket entity);
	public boolean insertTicketWithNewId(Ticket entity);
	public Long insertTicketAndGetNewId(Ticket entity);
	public void deleteTicketById(Long id);
	public boolean existsById(Long id);
}
