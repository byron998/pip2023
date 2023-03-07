package com.ibm.webdev.app.wink.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
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

import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.server.utils.LinkBuilders;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson2.JSON;
import com.ibm.webdev.app.jpa.dao.Cinema;
import com.ibm.webdev.app.jpa.dao.Movie;
import com.ibm.webdev.app.jpa.dao.Schedul;
import com.ibm.webdev.app.jpa.service.CinemaService;
import com.ibm.webdev.app.jpa.service.MovieService;
import com.ibm.webdev.app.jpa.service.SchedulService;
import com.ibm.webdev.app.wink.caching.SchedulCaching;
import com.ibm.webdev.app.wink.dto.SchedulDto;
import com.ibm.webdev.app.wink.dto.Validgroup;

@Workspace(workspaceTitle = "Schedul Service", collectionTitle = "My Schedul")
@Path("/scheduls")
public class SchedulResource {
    private static final String SUB_RESOURCE_PATH = "{schedul}";
    private static final String SUB_MOVIE_RESOURCE_PATH = "movie/{movie}";
    private static final String SUB_CINEMA_RESOURCE_PATH = "cinema/{cinema}";
    private static final String SUB_MOVIE_CINEMA_RESOURCE_PATH = "movie/{movie}/cinema/{cinema}";
    @Autowired
    private Validator validator;
    
	@Autowired
    private SchedulService service;
	
	@Autowired
    private MovieService movieService;
	
	@Autowired
    private CinemaService cinemaService;
	
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONArray getScheduls(@Context LinkBuilders linkProcessor, @Context UriInfo uriInfo) throws JSONException{
    	this.initialize();
    	JSONArray json= new JSONArray();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	JSONObject obj = new JSONObject();
        	obj.put("content", new JSONObject(scheduls.get(key)));
        	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
        	obj.put("link", location);
        	json.put(obj);
        }
        return json;
    }
    
    @Path(SUB_MOVIE_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONArray getSchedulsByMovie(@Context LinkBuilders linkProcessor, @Context UriInfo uriInfo,
    						@Valid @NotEmpty @PathParam("movie") String movieId) throws JSONException{
    	this.initialize();
    	JSONArray json= new JSONArray();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	if (scheduls.get(key).getMovieId().equals(movieId)) {
	        	JSONObject obj = new JSONObject();
	        	obj.put("content", new JSONObject(scheduls.get(key)));
	        	URI location = uriInfo.getBaseUriBuilder().segment("scheduls").segment(key).build();
	        	obj.put("link", location);
	        	json.put(obj);
        	}
        }
        if (json.length() == 0) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return json;
    }
    
    @Path(SUB_CINEMA_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONArray getSchedulsByCinema(@Context LinkBuilders linkProcessor, @Context UriInfo uriInfo,
    						@Valid @NotEmpty @PathParam("cinema") String cinemaId) throws JSONException{
    	this.initialize();
    	JSONArray json= new JSONArray();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	if (scheduls.get(key).getCinemaId().equals(cinemaId)) {
	        	JSONObject obj = new JSONObject();
	        	obj.put("content", new JSONObject(scheduls.get(key)));
	        	URI location = uriInfo.getBaseUriBuilder().segment("scheduls").segment(key).build();
	        	obj.put("link", location);
	        	json.put(obj);
        	}
        }
        if (json.length() == 0) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return json;
    }
    
    @Path(SUB_MOVIE_CINEMA_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONArray getSchedulsByMovieAndCinema(@Context LinkBuilders linkProcessor, @Context UriInfo uriInfo,
    						@Valid @NotEmpty @PathParam("movie")  String movieId,
    						@Valid @NotEmpty @PathParam("cinema") String cinemaId) throws JSONException{
    	this.initialize();
    	JSONArray json= new JSONArray();
        Map<String, SchedulDto> scheduls = SchedulCaching.getInstance().getScheduls();
       
        for (String key : scheduls.keySet()) {
        	if (scheduls.get(key).getMovieId().equals(movieId) && scheduls.get(key).getCinemaId().equals(cinemaId)) {
	        	JSONObject obj = new JSONObject();
	        	obj.put("content", new JSONObject(scheduls.get(key)));
	        	URI location = uriInfo.getBaseUriBuilder().segment("scheduls").segment(key).build();
	        	obj.put("link", location);
	        	json.put(obj);
        	}
        }
        if (json.length() == 0) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return json;
    }
    
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject createSchedul(JSONObject content,
                                   @Context UriInfo uriInfo,
                                   @Context LinkBuilders linkProcessor) throws JSONException  {
    	this.initialize();
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        SchedulDto dto;
        try {
        	dto = JSON.parseObject(content.toString(), SchedulDto.class);
            //不能为空
        	if (dto == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            Set<ConstraintViolation<SchedulDto>> sets = validator.validate(dto, Validgroup.insert.class);
            //合规检查
            if (!sets.isEmpty()) {
            	StringBuffer sb = new StringBuffer();
            	for (ConstraintViolation<SchedulDto> cons : sets) {
            		sb.append(cons.getMessage());
            	}
            	throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
            }
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        //new ID
        String schedulId = SchedulCaching.getNewId();
        dto.setId(schedulId);
        
        // 插入新的缓存记录
        SchedulCaching.getInstance().putSchedul(schedulId, dto);
        // 插入数据库
        Schedul insertdata = new Schedul();
        BeanUtils.copyProperties(dto, insertdata);
        insertdata.setId(Long.valueOf(schedulId));
        boolean insertResult = service.insertSchedulWithNewId(insertdata);
        if (!insertResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment(schedulId).build();

        JSONObject json = new JSONObject();
        json.put("action", "inserted");
        json.put("content", new JSONObject(dto));
        json.put("link", location);
        return json;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject getSchedul(@Context LinkBuilders linkProcessor,
                               @Context UriInfo uriInfo,
                               @PathParam("schedul") String schedulId) throws JSONException  {
    	this.initialize();
    	// 检查是否存在
    	SchedulDto schedul = SchedulCaching.getInstance().getSchedul(schedulId);
        if (schedul == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return new JSONObject(schedul);
    }

    @Path(SUB_RESOURCE_PATH)
    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject updateSchedul(JSONObject content,
                                    @Context LinkBuilders linkProcessor,
                                    @Context UriInfo uriInfo,
                                    @PathParam("schedul") String schedulId) throws JSONException {
    	this.initialize();
    	// 检查是否存在
    	SchedulDto value = SchedulCaching.getInstance().getSchedul(schedulId);
        if (value == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        SchedulDto dto;
        try {
        	dto = JSON.parseObject(content.toString(), SchedulDto.class);
            //不能为空
        	if (dto == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            Set<ConstraintViolation<SchedulDto>> sets = validator.validate(dto, Validgroup.update.class);
            //合规检查
            if (!sets.isEmpty()) {
            	StringBuffer sb = new StringBuffer();
            	for (ConstraintViolation<SchedulDto> cons : sets) {
            		sb.append(cons.getMessage());
            	}
            	throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
            }
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        // 更新缓存
        SchedulCaching.getInstance().putSchedul(schedulId, dto);
        // 更新数据库
        Schedul updatedata = new Schedul();
        BeanUtils.copyProperties(dto, updatedata);
        updatedata.setId(Long.valueOf(schedulId));
        Movie updatemovie = movieService.findMovieById(Long.valueOf(dto.getMovieId()));
        if (updatemovie == null || updatemovie.getId() == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        updatedata.setMovie(updatemovie);
        Cinema updatecinema = cinemaService.findCinemaById(Long.valueOf(dto.getCinemaId()));
        if (updatecinema == null || updatecinema.getId() == null) throw new WebApplicationException(Response.Status.NOT_FOUND);
        updatedata.setCinema(updatecinema);
        boolean updateResult = service.updateSchedulById(updatedata);
        if (!updateResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment().build();
        
        JSONObject json = new JSONObject();
        json.put("action", "updated");
        json.put("content", new JSONObject(dto));
        json.put("link", location);
        return json;
    }

    @Path(SUB_RESOURCE_PATH)
    @DELETE
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject deleteSchedul(@Context LinkBuilders linkProcessor,
                                    @Context UriInfo uriInfo,
                                    @PathParam("schedul") String schedulId) throws JSONException {
    	this.initialize();
    	JSONObject json = new JSONObject();
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
        json.put("action", "deleted");
        json.put("link", location);
        return json;
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
