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

import com.alibaba.fastjson2.JSON;
import com.ibm.webdev.app.jpa.dao.Cinema;
import com.ibm.webdev.app.jpa.service.CinemaService;
import com.ibm.webdev.app.wink.caching.CinemaCaching;
import com.ibm.webdev.app.wink.dto.CinemaDto;
import com.ibm.webdev.app.wink.dto.Validgroup;

@Workspace(workspaceTitle = "Cinema Service", collectionTitle = "My Cinema")
@Path("/cinemas")
public class CinemaResource {
    private static final String SUB_RESOURCE_PATH = "{cinema}";
    
    @Autowired
    private Validator validator;
    
	@Autowired
    private CinemaService service;
		
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONArray getCinemas(@Context LinkBuilders linkProcessor, @Context UriInfo uriInfo) throws JSONException{
    	this.initialize();
    	JSONArray json= new JSONArray();
        Map<String, CinemaDto> cinemas = CinemaCaching.getInstance().getCinemas();
       
        for (String key : cinemas.keySet()) {
        	JSONObject obj = new JSONObject();
        	obj.put("content", new JSONObject(cinemas.get(key)));
        	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
        	obj.put("link", location);
        	json.put(obj);
        }
        return json;
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject createCinema(JSONObject content,
                                   @Context UriInfo uriInfo,
                                   @Context LinkBuilders linkProcessor) throws JSONException  {
    	this.initialize();
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        CinemaDto dto;
        try {
        	dto = JSON.parseObject(content.toString(), CinemaDto.class);
            //不能为空
        	if (dto == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            Set<ConstraintViolation<CinemaDto>> sets = validator.validate(dto, Validgroup.insert.class);
            //合规检查
            if (!sets.isEmpty()) {
            	StringBuffer sb = new StringBuffer();
            	for (ConstraintViolation<CinemaDto> cons : sets) {
            		sb.append(cons.getMessage());
            	}
            	throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
            }
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        //new ID
        String cinemaId = CinemaCaching.getNewId();
        dto.setId(cinemaId);
        
        // 插入新的缓存记录
        CinemaCaching.getInstance().putCinema(cinemaId, dto);
        // 插入数据库
        Cinema insertdata = new Cinema();
        BeanUtils.copyProperties(dto, insertdata);
        insertdata.setId(Long.valueOf(cinemaId));
        boolean insertResult = service.insertCinemaWithNewId(insertdata);
        if (!insertResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment(cinemaId).build();

        JSONObject json = new JSONObject();
        json.put("action", "inserted");
        json.put("content", new JSONObject(dto));
        json.put("link", location);
        return json;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject getCinema(@Context LinkBuilders linkProcessor,
                               @Context UriInfo uriInfo,
                               @PathParam("cinema") String cinemaId) throws JSONException  {
    	this.initialize();
    	// 检查是否存在
    	CinemaDto cinema = CinemaCaching.getInstance().getCinema(cinemaId);
        if (cinema == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return new JSONObject(cinema);
    }

    @Path(SUB_RESOURCE_PATH)
    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject updateCinema(JSONObject content,
                                    @Context LinkBuilders linkProcessor,
                                    @Context UriInfo uriInfo,
                                    @PathParam("cinema") String cinemaId) throws JSONException {
    	this.initialize();
    	// 检查是否存在
    	CinemaDto value = CinemaCaching.getInstance().getCinema(cinemaId);
        if (value == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        CinemaDto dto;
        try {
        	dto = JSON.parseObject(content.toString(), CinemaDto.class);
            //不能为空
        	if (dto == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            Set<ConstraintViolation<CinemaDto>> sets = validator.validate(dto, Validgroup.update.class);
            //合规检查
            if (!sets.isEmpty()) {
            	StringBuffer sb = new StringBuffer();
            	for (ConstraintViolation<CinemaDto> cons : sets) {
            		sb.append(cons.getMessage());
            	}
            	throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
            }
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        // 更新缓存
        CinemaCaching.getInstance().putCinema(cinemaId, dto);
        // 更新数据库
        Cinema updatedata = new Cinema();
        BeanUtils.copyProperties(dto, updatedata);
        updatedata.setId(Long.valueOf(cinemaId));
        boolean updateResult = service.updateCinemaById(updatedata);
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
    public JSONObject deleteCinema(@Context LinkBuilders linkProcessor,
                                    @Context UriInfo uriInfo,
                                    @PathParam("cinema") String cinemaId) throws JSONException {
    	this.initialize();
    	JSONObject json = new JSONObject();
    	// 检查是否存在
    	CinemaDto cinema = CinemaCaching.getInstance().getCinema(cinemaId);
        if (cinema == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // 删除缓存
        CinemaCaching.getInstance().deleteCinema(cinemaId);
        // 删除数据库
        service.deleteCinemaById(Long.valueOf(cinemaId));
        
        URI location = uriInfo.getAbsolutePathBuilder().segment(cinema.getId()).build();
        json.put("action", "deleted");
        json.put("link", location);
        return json;
    }
    
    /**
     * 初期化缓存
     */
    public void initialize() {
    	if(CinemaCaching.getInstance().getCinemas().isEmpty()) {
    		List<Cinema> datalist = service.findAllCinemas();
			List<CinemaDto> instancelist = new ArrayList<>();
			for(Cinema data : datalist) {
				CinemaDto dto = new CinemaDto();
				BeanUtils.copyProperties(data, dto);
				dto.setId(String.valueOf(data.getId()));
				instancelist.add(dto);
			}
			CinemaCaching.getInstance(instancelist);
    	}
    }
}
