package com.ibm.webdev.app.jpa.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.JPAUtils;
import com.ibm.webdev.app.jpa.dao.Movie;
import com.ibm.webdev.app.jpa.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {
//    @Autowired
//    private MovieRepository repo;
    
    @Override
    public List<Movie> findAllMovies() {
        EntityManager em = null;
    	List<Movie> retList = null;
        try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	retList = em.createQuery("Select * from movie", Movie.class).getResultList();
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {  
        	em.close();  
        }
    	//repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Movie findMovieById(Long id) {
    	EntityManager em = null;
    	try{  
          	em=JPAUtils.getEntityManager();
          	// Query Data
          	return em.find(Movie.class, id);
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        } finally {  
        	em.close();  
        }
    }
    
    @Override
    public boolean updateMovieById(Movie entity) {

        EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	//执行操作
          	//Movie found = em.find(Movie.class, entity.getId());
          	Movie refer = em.getReference(Movie.class, entity.getId());
          	if (refer == null || refer.getId() == null) {
          		return false;
          	}
          	// update value
          	refer.setActor(entity.getActor());
          	refer.setDirector(entity.getDirector());
          	refer.setName(entity.getName());
          	refer.setPlacard(entity.getPlacard());
          	refer.setYear(entity.getYear());
          	refer.setMins(entity.getMins());
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
    public boolean insertMovieWithNewId(Movie entity) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Movie found;
          	if (entity.getId() != null) {
	          	//执行操作
	          	found = em.find(Movie.class, entity.getId());
	          	//Movie refer = em.getReference(Movie.class, entity.getId());
	          	if (found != null && found.getId() != null) {
	          		return false;
	          	}
          	}
          	else {
          		found = new Movie();
          	}
          	// insert value
          	found.setActor(entity.getActor());
          	found.setDirector(entity.getDirector());
          	found.setName(entity.getName());
          	found.setPlacard(entity.getPlacard());
          	found.setYear(entity.getYear());
          	found.setMins(entity.getMins());
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
    public void deleteMovieById(Long id) {
    	EntityManager em = null;  
        EntityTransaction tx = null;  
        try{  
          	em = JPAUtils.getEntityManager();
          	tx = em.getTransaction();
          	//Transaction 
          	tx.begin();
          	Movie found = em.find(Movie.class, id);
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
