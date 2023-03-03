package com.ibm.webdev.app.jpa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.dao.Schedul;
import com.ibm.webdev.app.jpa.repository.SchedulRepository;
import com.ibm.webdev.app.jpa.service.SchedulService;

@Service
public class SchedulServiceImpl implements SchedulService {
    @Autowired
    private SchedulRepository repo;
    
    @Override
    public List<Schedul> findAllScheduls() {
    	List<Schedul> retList = new ArrayList<Schedul>();
    	repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Schedul findSchedulById(Long id) {
    	return repo.findById(id).get();
    }
    
    @Override
    public boolean updateSchedulById(Schedul entity) {
    	if(entity.getId() != null && repo.existsById(entity.getId())) {
    		repo.save(entity);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public boolean insertSchedulWithNewId(Schedul entity) {
    	if(entity.getId() != null && repo.existsById(entity.getId())) {
    		return false;
    	}
    	else {
    		repo.save(entity);
    		return true;
    	}
    }
    
    @Override
    public void deleteSchedulById(Long id) {
    	repo.deleteById(id);
    }

	@Override
	public List<Schedul> findSchedulsByMovieId(Long movieId) {
		return repo.findByMovieId(movieId);
	}

	@Override
	public List<Schedul> findSchedulsByCinemaId(Long cinemaId) {
		return repo.findByCinemaId(cinemaId);
	}

	@Override
	public List<Schedul> findSchedulsByMovidIdCinemaId(Long movieId, Long cinemaId) {
		return repo.findByMovieIdCinemaId(movieId, cinemaId);
	}
	
	@Override
	public boolean existsById(Long id) {
		return repo.existsById(id);
	}
}
