package com.ibm.webdev.app.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.webdev.app.jpa.dao.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

}
