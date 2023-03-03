package com.ibm.webdev.app.jpa.service;

import java.util.List;

import com.ibm.webdev.app.jpa.dao.Schedul;

public interface SchedulService {
	public List<Schedul> findAllScheduls();
	public List<Schedul> findSchedulsByMovieId(Long movieId);
	public List<Schedul> findSchedulsByCinemaId(Long cinemaId);
	public List<Schedul> findSchedulsByMovidIdCinemaId(Long movieId, Long cinemaId);
	public Schedul findSchedulById(Long id);
	public boolean updateSchedulById(Schedul entity);
	public boolean insertSchedulWithNewId(Schedul entity);
	public void deleteSchedulById(Long id);
	public boolean existsById(Long id);
}
