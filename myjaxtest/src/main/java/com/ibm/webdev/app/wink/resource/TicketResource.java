package com.ibm.webdev.app.wink.resource;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.ibm.webdev.app.jpa.dao.Schedul;
import com.ibm.webdev.app.jpa.dao.Ticket;
import com.ibm.webdev.app.jpa.service.SchedulService;
import com.ibm.webdev.app.jpa.service.TicketService;
import com.ibm.webdev.app.websocket.client.MyWebSocketClient;
import com.ibm.webdev.app.wink.dto.TicketDto;
import com.ibm.webdev.app.wink.dto.Validgroup;

@Workspace(workspaceTitle = "Ticket Service", collectionTitle = "My Ticket")
@Path("/tickets")
public class TicketResource {
    private static final String SUB_RESOURCE_PATH = "{ticket}";
    
    @Autowired
    private Validator validator;
    
	@Autowired
    private TicketService service;
	
	@Autowired
    private SchedulService schedulService;
	
	private MyWebSocketClient client;
	private URI uri = URI.create("ws://localhost:8081/websocket?type=ws-publisher&clientId=admin");
	public TicketResource() {
		client = new MyWebSocketClient(uri);
        try {
            // 在连接成功之前会一直阻塞
            client.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONArray getTickets(@Context LinkBuilders linkProcessor, @Context UriInfo uriInfo) throws JSONException{
    	JSONArray json= new JSONArray();
    	List<Ticket> datalist = service.findAllTickets();
		for(Ticket data : datalist) {
			TicketDto dto = new TicketDto();
			BeanUtils.copyProperties(data, dto);
			dto.setId(String.valueOf(data.getId()));
			dto.setSchedulId(String.valueOf(data.getSchedul().getId()));
        	JSONObject obj = new JSONObject();
        	obj.put("content", new JSONObject(dto));
        	URI location = uriInfo.getAbsolutePathBuilder().segment(dto.getId()).build();
        	obj.put("link", location);
        	json.put(obj);
		}
        return json;
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject createTicket(JSONObject content,
                                   @Context UriInfo uriInfo,
                                   @Context LinkBuilders linkProcessor) throws JSONException  {
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        TicketDto dto;
        try {
        	dto = JSON.parseObject(content.toString(), TicketDto.class);
            //不能为空
        	if (dto == null) throw new WebApplicationException(Response.Status.BAD_REQUEST);
            Set<ConstraintViolation<TicketDto>> sets = validator.validate(dto, Validgroup.insert.class);
            //合规检查
            if (!sets.isEmpty()) {
            	StringBuffer sb = new StringBuffer();
            	for (ConstraintViolation<TicketDto> cons : sets) {
            		sb.append(cons.getMessage());
            	}
            	throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
            }
        } catch(Exception ex) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        // 是否存在
        Schedul insertSchedul = schedulService.findSchedulById(Long.valueOf(dto.getSchedulId()));
        if (insertSchedul == null || insertSchedul.getId() == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        // 插入数据库
        Ticket insertdata = new Ticket();
        BeanUtils.copyProperties(dto, insertdata);
        insertdata.setSchedul(insertSchedul);
        Long insertedId = service.insertTicketAndGetNewId(insertdata);
        if (insertedId == null || insertedId == 0L) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);
        dto.setId(String.valueOf(insertedId));
        URI location = uriInfo.getAbsolutePathBuilder().segment(dto.getId()).build();

        // 公布新排行
        client.send(insertSchedul.getMovie().getName()+":"+insertSchedul.getPrice());
        
        JSONObject json = new JSONObject();
        json.put("action", "inserted");
        json.put("content", new JSONObject(dto));
        json.put("link", location);
        return json;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject getTicket(@Context LinkBuilders linkProcessor,
                               @Context UriInfo uriInfo,
                               @PathParam("ticket") String ticketId) throws JSONException  {
    	// 检查是否存在
    	Ticket ticket = service.findTicketById(Long.valueOf(ticketId));
        if (ticket == null || ticket.getId() == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        TicketDto dto = new TicketDto();
        BeanUtils.copyProperties(ticket, dto);
        dto.setId(ticketId);
        dto.setSchedulId(String.valueOf(ticket.getSchedul().getId()));
        return new JSONObject(ticket);
    }

    @Path(SUB_RESOURCE_PATH)
    @DELETE
    @Produces( MediaType.APPLICATION_JSON )
    public JSONObject deleteTicket(@Context LinkBuilders linkProcessor,
                                    @Context UriInfo uriInfo,
                                    @PathParam("ticket") String ticketId) throws JSONException {
    	JSONObject json = new JSONObject();
    	// 检查是否存在
        if (!service.existsById(Long.valueOf(ticketId))) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // 删除数据库
        service.deleteTicketById(Long.valueOf(ticketId));
        
        URI location = uriInfo.getAbsolutePathBuilder().segment(ticketId).build();
        json.put("action", "deleted");
        json.put("link", location);
        return json;
    }
}
