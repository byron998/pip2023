package com.ibm.webdev.app.jpa.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.JPAUtils;
import com.ibm.webdev.app.jpa.dao.Cinema;
import com.ibm.webdev.app.jpa.service.CinemaService;

@Service
public class CinemaServiceImpl implements CinemaService {
//    @Autowired
//    private CinemaRepository repo;
    
    @Override
    public List<Cinema> findAllCinemas() {
        EntityManager em = null;
    	List<Cinema> retList = null;
        try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	retList = em.createQuery("Select * from cinema", Cinema.class).getResultList();
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {  
        	em.close();  
        }
    	//repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Cinema findCinemaById(Long id) {
    	EntityManager em = null;
    	try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	return em.find(Cinema.class, id);
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        } finally {  
        	em.close();  
        }
    }
    
    @Override
    public boolean updateCinemaById(Cinema entity) {

        EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	//执行操作
          	//Cinema found = em.find(Cinema.class, entity.getId());
          	Cinema refer = em.getReference(Cinema.class, entity.getId());
          	if (refer == null || refer.getId() == null) {
          		return false;
          	}
          	// update value
          	refer.setAddress(entity.getAddress());
          	refer.setCity(entity.getCity());
          	refer.setName(entity.getName());
          	refer.setHallcount(entity.getHallcount());
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
    public boolean insertCinemaWithNewId(Cinema entity) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Cinema found;
          	if (entity.getId() != null) {
	          	//执行操作
	          	found = em.find(Cinema.class, entity.getId());
	          	//Cinema refer = em.getReference(Cinema.class, entity.getId());
	          	if (found != null && found.getId() != null) {
	          		return false;
	          	}
          	}
          	else {
          		found = new Cinema();
          	}
          	// insert value
          	found.setAddress(entity.getAddress());
          	found.setCity(entity.getCity());
          	found.setName(entity.getName());
          	found.setHallcount(entity.getHallcount());
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
    public void deleteCinemaById(Long id) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Cinema found = em.find(Cinema.class, id);
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
