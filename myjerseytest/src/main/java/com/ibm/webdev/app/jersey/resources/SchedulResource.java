package com.ibm.webdev.app.jersey.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.webdev.app.jersey.caching.SchedulCaching;
import com.ibm.webdev.app.jersey.resources.dto.SchedulDto;
import com.ibm.webdev.app.jersey.resources.resp.ActReesponseRest;
import com.ibm.webdev.app.jpa.dao.Cinema;
import com.ibm.webdev.app.jpa.dao.Movie;
import com.ibm.webdev.app.jpa.dao.Schedul;
import com.ibm.webdev.app.jpa.service.CinemaService;
import com.ibm.webdev.app.jpa.service.MovieService;
import com.ibm.webdev.app.jpa.service.SchedulService;

@Path("/scheduls")
public class SchedulResource {
    private static final String SUB_RESOURCE_PATH = "{schedul}";
    private static final String SUB_MOVIE_RESOURCE_PATH = "movie/{movie}";
    private static final String SUB_CINEMA_RESOURCE_PATH = "cinema/{cinema}";
    private static final String SUB_MOVIE_CINEMA_RESOURCE_PATH = "movie/{movie}/cinema/{cinema}";
    
	@Autowired
    private SchedulService service;
	
	@Autowired
    private MovieService movieService;
	
	@Autowired
    private CinemaService cinemaService;
	
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<ActReesponseRest> getScheduls(@Context UriInfo uriInfo) throws Exception{
    	this.initialize();
    	List<ActReesponseRest> restList = new ArrayList<ActReesponseRest>();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	ActReesponseRest obj = new ActReesponseRest();
        	obj.setContent(scheduls.get(key));
        	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
        	obj.setLink(location);
        	restList.add(obj);
        }
        return restList;
    }
    
    @Path(SUB_MOVIE_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<ActReesponseRest> getSchedulsByMovie(@Context UriInfo uriInfo,
    						@Valid @NotEmpty @PathParam("movie") String movieId) throws Exception{
    	this.initialize();
    	List<ActReesponseRest> restist= new ArrayList<ActReesponseRest>();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	if (scheduls.get(key).getMovieId().equals(movieId)) {
        		ActReesponseRest obj = new ActReesponseRest();
	        	obj.setContent(scheduls.get(key));
	        	URI location = uriInfo.getBaseUriBuilder().segment("scheduls").segment(key).build();
	        	obj.setLink(location);
	        	restist.add(obj);
        	}
        }
        if (restist.size() == 0) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return restist;
    }
    
    @Path(SUB_CINEMA_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<ActReesponseRest> getSchedulsByCinema(@Context UriInfo uriInfo,
    						@Valid @NotEmpty @PathParam("cinema") String cinemaId) throws Exception{
    	this.initialize();
    	List<ActReesponseRest> restList = new ArrayList<ActReesponseRest>();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	if (scheduls.get(key).getCinemaId().equals(cinemaId)) {
        		ActReesponseRest obj = new ActReesponseRest();
            	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
        		obj.setAction("getone");
        		obj.setLink(location);
        		obj.setContent(scheduls.get(key));
        		restList.add(obj);
        	}
        }
        if (restList.size() == 0) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return restList;
    }
    
    @Path(SUB_MOVIE_CINEMA_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<ActReesponseRest> getSchedulsByMovieAndCinema(@Context UriInfo uriInfo,
    						@Valid @NotEmpty @PathParam("movie")  String movieId,
    						@Valid @NotEmpty @PathParam("cinema") String cinemaId) throws Exception{
    	this.initialize();
    	List<ActReesponseRest> restList = new ArrayList<ActReesponseRest>();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	if (scheduls.get(key).getMovieId().equals(movieId) && scheduls.get(key).getCinemaId().equals(cinemaId)) {
        		ActReesponseRest obj = new ActReesponseRest();
            	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
            	obj.setAction("getone");
        		obj.setLink(location);
        		obj.setContent(scheduls.get(key));
        		restList.add(obj);
        	}
        }
        if (restList.size() == 0) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return restList;
    }
    
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest createSchedul(@Valid SchedulDto content,
                                   @Context UriInfo uriInfo) throws Exception  {
    	this.initialize();
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        //new ID
        String schedulId = SchedulCaching.getNewId();
        content.setId(schedulId);
        
        // 插入新的缓存记录
        SchedulCaching.getInstance().putSchedul(schedulId, content);
        // 插入数据库
        Schedul insertdata = new Schedul();
        BeanUtils.copyProperties(content, insertdata);
        insertdata.setId(Long.valueOf(schedulId));
        boolean insertResult = service.insertSchedulWithNewId(insertdata);
        if (!insertResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment(schedulId).build();

        ActReesponseRest rest = new ActReesponseRest();
        rest.setAction("inserted");
        rest.setContent(content);
        rest.setLink(location);
        return rest;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public SchedulDto getSchedul(@Context UriInfo uriInfo,
                               @PathParam("schedul") String schedulId) throws Exception  {
    	this.initialize();
    	// 检查是否存在
    	SchedulDto schedul = SchedulCaching.getInstance().getSchedul(schedulId);
        if (schedul == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return (schedul);
    }

    @Path(SUB_RESOURCE_PATH)
    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest updateSchedul(@Valid SchedulDto content,
                                    @Context UriInfo uriInfo,
                                    @PathParam("schedul") String schedulId) throws Exception {
    	this.initialize();
    	// 检查是否存在
    	SchedulDto value = SchedulCaching.getInstance().getSchedul(schedulId);
        if (value == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // 更新缓存
        SchedulCaching.getInstance().putSchedul(schedulId, content);
        // 更新数据库
        Schedul updatedata = new Schedul();
        BeanUtils.copyProperties(content, updatedata);
        updatedata.setId(Long.valueOf(schedulId));
        Movie updatemovie = movieService.findMovieById(Long.valueOf(content.getMovieId()));
        if (updatemovie == null || updatemovie.getId() == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        updatedata.setMovie(updatemovie);
        Cinema updatecinema = cinemaService.findCinemaById(Long.valueOf(content.getCinemaId()));
        if (updatecinema == null || updatecinema.getId() == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        updatedata.setCinema(updatecinema);
        boolean updateResult = service.updateSchedulById(updatedata);
        if (!updateResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment().build();
        
        ActReesponseRest obj = new ActReesponseRest();
        obj.setAction("updated");
        obj.setContent(content);
        obj.setLink(location);
        return obj;
    }

    @Path(SUB_RESOURCE_PATH)
    @DELETE
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest deleteSchedul(@Context UriInfo uriInfo,
                                    @PathParam("schedul") String schedulId) throws Exception {
    	this.initialize();
    	ActReesponseRest obj = new ActReesponseRest();
    	// 检查是否存在
    	SchedulDto schedul = SchedulCaching.getInstance().getSchedul(schedulId);
        if (schedul == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // 删除缓存
        SchedulCaching.getInstance().deleteSchedul(schedulId);
        // 删除数据库
        service.deleteSchedulById(Long.valueOf(schedulId));
        
        URI location = uriInfo.getAbsolutePathBuilder().segment(schedul.getId()).build();
        obj.setAction("deleted");
        obj.setLink(location);
        return obj;
    }
    
    /**
     * 初期化缓存
     */
    public void initialize() {
    	if(SchedulCaching.getInstance().getScheduls().isEmpty()) {
    		List<Schedul> datalist = service.findAllScheduls();
			List<SchedulDto> instancelist = new ArrayList<>();
			for(Schedul data : datalist) {
				SchedulDto dto = new SchedulDto();
				BeanUtils.copyProperties(data, dto);
				dto.setId(String.valueOf(data.getId()));
				dto.setMovieId(String.valueOf(data.getMovie().getId()));
				dto.setCinemaId(String.valueOf(data.getCinema().getId()));
				instancelist.add(dto);
			}
			SchedulCaching.getInstance(instancelist);
    	}
    }
}
