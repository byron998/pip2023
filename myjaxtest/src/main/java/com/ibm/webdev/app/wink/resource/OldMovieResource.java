package com.ibm.webdev.app.wink.resource;

import java.net.URI;
import java.util.Map;

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

import com.alibaba.fastjson2.JSON;
import com.ibm.webdev.app.wink.caching.MovieCaching;
import com.ibm.webdev.app.wink.dto.MovieDto;

@Workspace(workspaceTitle = "Old Movie Service", collectionTitle = "My Old Movie")
@Path("/old/movies")
public class OldMovieResource {
    private static final String SUB_RESOURCE_PATH = "{movie}";
		
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
    public JSONObject createMovie(JSONObject content,
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
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        //new ID
        String movieId = MovieCaching.getNewId();
        dto.setId(movieId);
        
        // 插入新的缓存记录
        MovieCaching.getInstance().putMovie(movieId, dto);

        URI location = uriInfo.getAbsolutePathBuilder().segment(movieId).build();

        JSONObject json = new JSONObject();
        json.put("action", "inserted");
        json.put("content", new JSONObject(dto));
        json.put("link", location);
        return json;
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
    public JSONObject updateMovie(JSONObject content,
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
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        // 更新缓存
        MovieCaching.getInstance().putMovie(movieId, dto);

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
    public JSONObject deleteMovie(@Context LinkBuilders linkProcessor,
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
        
        URI location = uriInfo.getAbsolutePathBuilder().segment(movie.getId()).build();
        json.put("action", "deleted");
        json.put("link", location);
        return json;
    }
    
    /**
     * 初期化缓存
     */
    public void initialize() {
    	if(MovieCaching.getInstance().getMovies().isEmpty()) {
			MovieCaching.getInstanceOld();
    	}
    }
}
