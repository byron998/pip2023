package com.ibm.webdev.app.jpa.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jpa.dao.Movie;
import com.ibm.webdev.app.jpa.repository.MovieRepository;
import com.ibm.webdev.app.jpa.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository repo;
    
    @Override
    public List<Movie> findAllMovies() {
    	List<Movie> retList = new ArrayList<Movie>();
    	repo.findAll().forEach(retList::add);
    	return retList;
    }
    
    @Override
    public Movie findMovieById(Long id) {
    	return repo.findById(id).get();
    }
    
    @Override
    public boolean updateMovieById(Movie entity) {
    	if(entity.getId() != null && repo.existsById(entity.getId())) {
    		repo.save(entity);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public boolean insertMovieWithNewId(Movie entity) {
    	if(entity.getId() != null && repo.existsById(entity.getId())) {
    		return false;
    	}
    	else {
    		repo.save(entity);//不带ID（如果ID不自动@GeneratedValue）的SAVE会抛异常：ids for this class must be manually assigned before calling save()
    		return true;
    	}
    }
    
    @Override
    public void deleteMovieById(Long id) {
    	repo.deleteById(id);
    }
    
	@Override
	public boolean existsById(Long id) {
		return repo.existsById(id);
	}
}
