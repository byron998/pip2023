package com.ibm.webdev.app.jpa.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.JPAUtils;
import com.ibm.webdev.app.jpa.dao.Schedul;
import com.ibm.webdev.app.jpa.service.SchedulService;

@Service
public class SchedulServiceImpl implements SchedulService {
//    @Autowired
//    private SchedulRepository repo;
    
    @Override
    public List<Schedul> findAllScheduls() {
        EntityManager em = null;
    	List<Schedul> retList = null;
        try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	retList = em.createQuery("Select * from schedul", Schedul.class).getResultList();
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {  
        	em.close();  
        }
    	//repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Schedul findSchedulById(Long id) {
    	EntityManager em = null;
    	try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	return em.find(Schedul.class, id);
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        } finally {  
        	em.close();  
        }
    }
    
    @Override
    public boolean updateSchedulById(Schedul entity) {

        EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	//执行操作
          	//Schedul found = em.find(Schedul.class, entity.getId());
          	Schedul refer = em.getReference(Schedul.class, entity.getId());
          	if (refer == null || refer.getId() == null) {
          		return false;
          	}
          	// update value
          	refer.setCinema(entity.getCinema());
          	refer.setMovie(entity.getMovie());
          	refer.setPrice(entity.getPrice());
          	refer.setHall(entity.getHall());
          	refer.setShowtime(entity.getShowtime());
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
    public boolean insertSchedulWithNewId(Schedul entity) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Schedul found;
          	if (entity.getId() != null) {
	          	//执行操作
	          	found = em.find(Schedul.class, entity.getId());
	          	//Schedul refer = em.getReference(Schedul.class, entity.getId());
	          	if (found != null && found.getId() != null) {
	          		return false;
	          	}
          	}
          	else {
          		found = new Schedul();
          	}
          	// insert value
          	found.setCinema(entity.getCinema());
          	found.setMovie(entity.getMovie());
          	found.setPrice(entity.getPrice());
          	found.setHall(entity.getHall());
          	found.setShowtime(entity.getShowtime());
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
    public void deleteSchedulById(Long id) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Schedul found = em.find(Schedul.class, id);
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
