package com.ibm.webdev.app.jpa.service;

import java.util.List;

import com.ibm.webdev.app.jpa.dao.Movie;

public interface MovieService {
	public List<Movie> findAllMovies();
	public Movie findMovieById(Long id);
	public boolean updateMovieById(Movie entity);
	public boolean insertMovieWithNewId(Movie entity);
	public void deleteMovieById(Long id);
	public boolean existsById(Long id);
}
