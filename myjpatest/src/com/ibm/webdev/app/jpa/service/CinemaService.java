package com.ibm.webdev.app.jpa.service;

import java.util.List;

import com.ibm.webdev.app.jpa.dao.Cinema;

public interface CinemaService {
	public List<Cinema> findAllCinemas();
	public Cinema findCinemaById(Long id);
	public boolean updateCinemaById(Cinema entity);
	public boolean insertCinemaWithNewId(Cinema entity);
	public void deleteCinemaById(Long id);
}
