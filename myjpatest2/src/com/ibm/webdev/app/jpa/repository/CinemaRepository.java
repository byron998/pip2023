package com.ibm.webdev.app.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.webdev.app.jpa.dao.Cinema;

@Repository
public interface CinemaRepository extends CrudRepository<Cinema, Long> {

}
