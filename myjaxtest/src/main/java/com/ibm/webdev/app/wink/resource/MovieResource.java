package com.ibm.webdev.app.wink.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.alibaba.fastjson2.JSON;
import com.ibm.webdev.app.jpa.dao.Movie;
import com.ibm.webdev.app.jpa.service.MovieService;
import com.ibm.webdev.app.wink.caching.MovieCaching;
import com.ibm.webdev.app.wink.dto.MovieDto;
import com.ibm.webdev.app.wink.dto.Validgroup;
import com.ibm.webdev.app.wink.response.ActReesponseRest;

@Workspace(workspaceTitle = "Movie Service", collectionTitle = "My Movie")
@Path("/movies")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MovieResource {
    private static final String SUB_RESOURCE_PATH = "{movie}";
    
    @Autowired
    private Validator validator;
    
	@Autowired
    private MovieService service;
		
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONArray getMovies(@Context LinkBuilders linkProcessor, @Context UriInfo uriInfo) throws JSONException{
    	this.initialize();
    	JSONArray json= new JSONArray();
        Map<String, MovieDto> movies = MovieCaching.getInstance().getMovies();
       
        for (String key : movies.keySet()) {
        	JSONObject obj = new JSONObject();
        	obj.put("content", new JSONObject(movies.get(key)));
        	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
        	obj.put("link", location);
        	json.put(obj);
        }
        return json;
    }
    
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest createMovie(JSONObject content,
                                   @Context UriInfo uriInfo,
                                   @Context LinkBuilders linkProcessor) throws JSONException  {
    	this.initialize();
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        MovieDto dto;
        try {
        	dto = JSON.parseObject(content.toString(), MovieDto.class);
            //不能为空
        	if (dto == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            Set<ConstraintViolation<MovieDto>> sets = validator.validate(dto, Validgroup.insert.class);
            //合规检查
            if (!sets.isEmpty()) {
            	StringBuffer sb = new StringBuffer();
            	for (ConstraintViolation<MovieDto> cons : sets) {
            		sb.append(cons.getMessage());
            	}
            	throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
            }
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        //new ID
        String movieId = MovieCaching.getNewId();
        dto.setId(movieId);
        
        // 插入新的缓存记录
        MovieCaching.getInstance().putMovie(movieId, dto);
        // 插入数据库
        Movie insertdata = new Movie();
        BeanUtils.copyProperties(dto, insertdata);
        //insertdata.setId(Long.valueOf(movieId));
        boolean insertResult = service.insertMovieWithNewId(insertdata);
        if (!insertResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment(movieId).build();

        ActReesponseRest rest = new ActReesponseRest();
        rest.setAction("inserted");
        rest.setContent(new JSONObject(dto));
        rest.setLink(location);
        return rest;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject getMovie(@Context LinkBuilders linkProcessor,
                               @Context UriInfo uriInfo,
                               @PathParam("movie") String movieId) throws JSONException  {
    	this.initialize();
    	// 检查是否存在
    	MovieDto movie = MovieCaching.getInstance().getMovie(movieId);
        if (movie == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return new JSONObject(movie);
    }

    @Path(SUB_RESOURCE_PATH)
    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest updateMovie(JSONObject content,
                                    @Context LinkBuilders linkProcessor,
                                    @Context UriInfo uriInfo,
                                    @PathParam("movie") String movieId) throws JSONException {
    	this.initialize();
    	// 检查是否存在
    	MovieDto value = MovieCaching.getInstance().getMovie(movieId);
        if (value == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        MovieDto dto;
        try {
        	dto = JSON.parseObject(content.toString(), MovieDto.class);
            //不能为空
        	if (dto == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            Set<ConstraintViolation<MovieDto>> sets = validator.validate(dto, Validgroup.update.class);
            //合规检查
            if (!sets.isEmpty()) {
            	StringBuffer sb = new StringBuffer();
            	for (ConstraintViolation<MovieDto> cons : sets) {
            		sb.append(cons.getMessage());
            	}
            	throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
            }
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        // 更新缓存
        MovieCaching.getInstance().putMovie(movieId, dto);
        // 更新数据库
        Movie updatedata = new Movie();
        BeanUtils.copyProperties(dto, updatedata);
        updatedata.setId(Long.valueOf(movieId));
        boolean updateResult = service.updateMovieById(updatedata);
        if (!updateResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment().build();
        
        ActReesponseRest rest = new ActReesponseRest();
        rest.setAction("updated");
        rest.setContent(new JSONObject(dto));
        rest.setLink(location);
        return rest;
    }

    @Path(SUB_RESOURCE_PATH)
    @DELETE
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest deleteMovie(@Context LinkBuilders linkProcessor,
                                    @Context UriInfo uriInfo,
                                    @PathParam("movie") String movieId) throws JSONException {
    	this.initialize();
    	JSONObject json = new JSONObject();
    	// 检查是否存在
    	MovieDto movie = MovieCaching.getInstance().getMovie(movieId);
        if (movie == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // 删除缓存
        MovieCaching.getInstance().deleteMovie(movieId);
        // 删除数据库
        service.deleteMovieById(Long.valueOf(movieId));
        
        URI location = uriInfo.getAbsolutePathBuilder().segment(movie.getId()).build();
        ActReesponseRest rest = new ActReesponseRest();
        rest.setAction("deleted");
        rest.setLink(location);
        return rest;
    }
    
    /**
     * 初期化缓存
     */
    public void initialize() {
    	if(MovieCaching.getInstance().getMovies().isEmpty()) {
    		List<Movie> datalist = service.findAllMovies();
			List<MovieDto> instancelist = new ArrayList<>();
			for(Movie data : datalist) {
				MovieDto dto = new MovieDto();
				BeanUtils.copyProperties(data, dto);
				dto.setId(String.valueOf(data.getId()));
				instancelist.add(dto);
			}
			MovieCaching.getInstance(instancelist);
    	}
    }
}
