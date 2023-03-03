package com.ibm.webdev.app.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ibm.webdev.app.jpa.dao.Schedul;

@Repository
public interface SchedulRepository extends CrudRepository<Schedul, Long> {
	@Query("SELECT s FROM Schedul s WHERE movie_id =:movieId")
	List<Schedul> findByMovieId(@Param("movieId") Long movieId);
	
	@Query("SELECT s FROM Schedul s WHERE cinema_id =:cinemaId")
	List<Schedul> findByCinemaId(@Param("cinemaId") Long cinemaId);
	
	@Query("SELECT s FROM Schedul s WHERE movie_id =:movieId AND cinema_id =:cinemaId")
	List<Schedul> findByMovieIdCinemaId(@Param("movieId") Long movieId, @Param("cinemaId") Long cinemaId);

}
