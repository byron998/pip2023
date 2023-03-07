package com.ibm.webdev.app.wink.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MovieDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @NotEmpty(message="{movie.id.isempty.error}", groups={Validgroup.update.class})
    String id;
    @NotEmpty(message="{movie.name.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
    @NotBlank(message="{movie.name.isblank.error}", groups={Validgroup.insert.class, Validgroup.update.class})
    String name;
    @NotBlank(message="{movie.director.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
    @NotEmpty(message="{movie.director.isblank.error}", groups={Validgroup.insert.class, Validgroup.update.class})
    String director;
    @NotBlank(message="{movie.actor.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
    @NotEmpty(message="{movie.actor.isblank.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	String actor;
	String placard;
	int year;
	int mins;
	
	public MovieDto() {
		
	}
	
	public MovieDto(String id, String name, String director, String actor,String placard, int year, int mins) {
		this.id = id;
		this.name = name;
		this.director = director;
		this.actor = actor;
		this.placard = placard;
		this.year = year;
		this.mins = mins;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getPlacard() {
		return placard;
	}

	public void setPlacard(String placard) {
		this.placard = placard;
	}

	public int getMins() {
		return mins;
	}

	public void setMins(int mins) {
		this.mins = mins;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
