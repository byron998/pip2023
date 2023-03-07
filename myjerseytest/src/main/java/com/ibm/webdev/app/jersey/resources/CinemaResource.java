package com.ibm.webdev.app.jersey.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
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

import com.ibm.webdev.app.jersey.caching.CinemaCaching;
import com.ibm.webdev.app.jersey.resources.dto.CinemaDto;
import com.ibm.webdev.app.jersey.resources.resp.ActReesponseRest;
import com.ibm.webdev.app.jpa.dao.Cinema;
import com.ibm.webdev.app.jpa.service.CinemaService;

@Path("/cinemas")
public class CinemaResource {
    private static final String SUB_RESOURCE_PATH = "{cinema}";
    
	@Autowired
    private CinemaService service;
		
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<ActReesponseRest> getCinemas(@Context UriInfo uriInfo) throws Exception{
    	this.initialize();
        Map<String, CinemaDto> cinemas = CinemaCaching.getInstance().getCinemas();
        List<ActReesponseRest> restList = new ArrayList<>();
        for (String key : cinemas.keySet()) {
        	ActReesponseRest obj = new ActReesponseRest();
        	obj.setContent(cinemas.get(key));
        	URI location = uriInfo.getAbsolutePathBuilder().segment(key).build();
        	obj.setLink(location);
        	restList.add(obj);
        }
        return restList;
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest createCinema(@Valid CinemaDto content,
                                   @Context UriInfo uriInfo) throws Exception  {
    	this.initialize();
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        //new ID
        String cinemaId = CinemaCaching.getNewId();
        content.setId(cinemaId);
        
        // 插入新的缓存记录
        CinemaCaching.getInstance().putCinema(cinemaId, content);
        // 插入数据库
        Cinema insertdata = new Cinema();
        BeanUtils.copyProperties(content, insertdata);
        insertdata.setId(Long.valueOf(cinemaId));
        boolean insertResult = service.insertCinemaWithNewId(insertdata);
        if (!insertResult) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);

        URI location = uriInfo.getAbsolutePathBuilder().segment(cinemaId).build();

        ActReesponseRest obj = new ActReesponseRest();
        obj.setAction("inserted");
        obj.setContent(content);
        obj.setLink(location);
        return obj;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public CinemaDto getCinema(@Context UriInfo uriInfo,
                               @PathParam("cinema") String cinemaId) throws Exception  {
    	this.initialize();
    	// 检查是否存在
    	CinemaDto cinema = CinemaCaching.getInstance().getCinema(cinemaId);
        if (cinema == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return cinema;
    }

    @Path(SUB_RESOURCE_PATH)
    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest updateCinema(@Valid CinemaDto content,
                                    @Context UriInfo uriInfo,
                                    @PathParam("cinema") String cinemaId) throws Exception {
    	this.initialize();
    	// 检查是否存在
    	CinemaDto value = CinemaCaching.getInstance().getCinema(cinemaId);
        if (value == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        // 更新缓存
        CinemaCaching.getInstance().putCinema(cinemaId, content);
        // 更新数据库
        Cinema updatedata = new Cinema();
        BeanUtils.copyProperties(content, updatedata);
        updatedata.setId(Long.valueOf(cinemaId));
        boolean updateResult = service.updateCinemaById(updatedata);
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
    public ActReesponseRest deleteCinema(@Context UriInfo uriInfo,
                                    @PathParam("cinema") String cinemaId) throws Exception {
    	this.initialize();
    	ActReesponseRest obj = new ActReesponseRest();
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
        obj.setAction("deleted");
        obj.setLink(location);
        return obj;
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
