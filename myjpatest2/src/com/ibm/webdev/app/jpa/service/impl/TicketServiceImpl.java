package com.ibm.webdev.app.jpa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.dao.Ticket;
import com.ibm.webdev.app.jpa.repository.TicketRepository;
import com.ibm.webdev.app.jpa.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository repo;
    
    @Override
    public List<Ticket> findAllTickets() {
    	List<Ticket> retList = new ArrayList<Ticket>();
    	repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Ticket findTicketById(Long id) {
    	return repo.findById(id).get();
    }
    
    @Override
    public boolean updateTicketById(Ticket entity) {
    	if(repo.existsById(entity.getId())) {
    		repo.save(entity);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public boolean insertTicketWithNewId(Ticket entity) {
    	if(entity.getId() != null || repo.existsById(entity.getId())) {
    		return false;
    	}
    	else {
    		repo.save(entity);
    		return true;
    	}
    }
    
    
    
    @Override
    public void deleteTicketById(Long id) {
    	repo.deleteById(id);
    }

	@Override
	public boolean existsById(Long id) {
		return repo.existsById(id);
	}

	@Override
	public Long insertTicketAndGetNewId(Ticket entity) {
		Ticket saved = repo.save(entity);
		return saved.getId();
	}
}
