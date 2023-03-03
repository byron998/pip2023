package com.ibm.webdev.app.jpa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.dao.Cinema;
import com.ibm.webdev.app.jpa.repository.CinemaRepository;
import com.ibm.webdev.app.jpa.service.CinemaService;

@Service
public class CinemaServiceImpl implements CinemaService {
    @Autowired
    private CinemaRepository repo;
    
    @Override
    public List<Cinema> findAllCinemas() {
    	List<Cinema> retList = new ArrayList<Cinema>();
    	repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Cinema findCinemaById(Long id) {
    	return repo.findById(id).get();
    }
    
    @Override
    public boolean updateCinemaById(Cinema entity) {
    	if(entity.getId() != null && repo.existsById(entity.getId())) {
    		repo.save(entity);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public boolean insertCinemaWithNewId(Cinema entity) {
    	if(entity.getId() != null && repo.existsById(entity.getId())) {
    		return false;
    	}
    	else {
    		repo.save(entity);
    		return true;
    	}
    }
    
    @Override
    public void deleteCinemaById(Long id) {
    	repo.deleteById(id);
    }
    
	@Override
	public boolean existsById(Long id) {
		return repo.existsById(id);
	}
}
