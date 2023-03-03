package com.ibm.webdev.app.jpa.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.JPAUtils;
import com.ibm.webdev.app.jpa.dao.Ticket;
import com.ibm.webdev.app.jpa.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {
//    @Autowired
//    private TicketRepository repo;
    
    @Override
    public List<Ticket> findAllTickets() {
        EntityManager em = null;
    	List<Ticket> retList = null;
        try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	retList = em.createQuery("Select * from ticket", Ticket.class).getResultList();
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {  
        	em.close();  
        }
    	//repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Ticket findTicketById(Long id) {
    	EntityManager em = null;
    	try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	return em.find(Ticket.class, id);
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        } finally {  
        	em.close();  
        }
    }
    
    @Override
    public boolean updateTicketById(Ticket entity) {

        EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	//执行操作
          	//Ticket found = em.find(Ticket.class, entity.getId());
          	Ticket refer = em.getReference(Ticket.class, entity.getId());
          	if (refer == null || refer.getId() == null) {
          		return false;
          	}
          	// update value
          	refer.setSchedul(entity.getSchedul());
          	refer.setPhone(entity.getPhone());
          	refer.setBookingtime(entity.getBookingtime());
          	em.clear();
          	em.merge(refer);
          	tx.commit();
          	return true;
        } catch(Exception e) {
          	// rollback
          	tx.rollback();
          	e.printStackTrace();
          	return false;
        } finally {
        	em.close();  
        }
    }
    
    @Override
    public boolean insertTicketWithNewId(Ticket entity) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Ticket found;
          	if (entity.getId() != null) {
	          	//执行操作
	          	found = em.find(Ticket.class, entity.getId());
	          	//Ticket refer = em.getReference(Ticket.class, entity.getId());
	          	if (found != null && found.getId() != null) {
	          		return false;
	          	}
          	}
          	else {
          		found = new Ticket();
          	}
          	// insert value
          	found.setSchedul(entity.getSchedul());
          	found.setPhone(entity.getPhone());
          	found.setBookingtime(entity.getBookingtime());
          	em.clear();
          	em.persist(found);
          	tx.commit();
          	return true;
        } catch(Exception e) {
          	// rollback
          	tx.rollback();
          	e.printStackTrace();
          	return false;
        } finally {
        	em.close();  
        }
    }
    
    @Override
    public void deleteTicketById(Long id) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Ticket found = em.find(Ticket.class, id);
          	em.clear();
          	em.remove(found);
          	tx.commit();
        } catch(Exception e) {
          	// rollback
          	tx.rollback();
          	e.printStackTrace();
        } finally {
        	em.close();  
        }
    }
}
