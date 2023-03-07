package com.ibm.webdev.app.wink.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SchedulDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="{schedul.id.isempty.error}", groups={Validgroup.update.class})
	private String id;
    @NotEmpty(message="{schedul.movieId.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	private String movieId;
    @NotEmpty(message="{schedul.cinemaId.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	private String cinemaId;
	@Min(value=1,  message="{schedul.hall.ismin.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	@Max(value=99, message="{schedul.hall.ismax.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	private int hall;
	@Min(value=1,   message="{schedul.price.ismin.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	@Max(value=999, message="{schedul.price.ismax.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	private int price;
	@NotNull(message="{schedul.showtime.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	private Timestamp showtime;
	public SchedulDto() {}
	public SchedulDto(String id, String movieId, String cinemaId, int hall, int price) {
		this.id = id;
		this.movieId = movieId;
		this.cinemaId = cinemaId;
		this.hall = hall;
		this.price = price;
		this.showtime = new Timestamp(0l);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	public int getHall() {
		return hall;
	}

	public void setHall(int hall) {
		this.hall = hall;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Timestamp getShowtime() {
		return showtime;
	}

	public void setShowtime(Timestamp showtime) {
		this.showtime = showtime;
	}
}
