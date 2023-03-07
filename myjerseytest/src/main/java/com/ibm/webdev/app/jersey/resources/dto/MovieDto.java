package com.ibm.webdev.app.jersey.resources.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("电影信息实体类")
public class MovieDto implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="电影ID", required=false)
	@NotEmpty(message = "{movie.id.isempty.error}", groups = { Validgroup.update.class })
	String id;
	@ApiModelProperty(value="电影名称", required=true)
	@NotBlank(message = "{movie.name.isblank.error}", groups = { Validgroup.insert.class, Validgroup.update.class,
			Default.class })
	String name;
	@ApiModelProperty(value="电影导演", required=true)
	@NotBlank(message = "{movie.director.isblank.error}", groups = { Validgroup.insert.class, Validgroup.update.class,
			Default.class })
	String director;
	@ApiModelProperty(value="电影主演", required=true)
	@NotBlank(message = "{movie.actor.isblank.error}", groups = { Validgroup.insert.class, Validgroup.update.class,
			Default.class })
	String actor;
	@ApiModelProperty(value="封面海报", required=false)
	String placard;
	@ApiModelProperty(value="上映年份", required=false)
	int year;
	@ApiModelProperty(value="片长（分钟）", required=false)
	int mins;

	public MovieDto() {

	}

	public MovieDto(String id, String name, String director, String actor, String placard, int year, int mins) {
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
