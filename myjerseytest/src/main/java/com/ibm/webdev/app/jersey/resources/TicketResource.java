package com.ibm.webdev.app.jersey.resources;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.webdev.app.jersey.resources.dto.TicketDto;
import com.ibm.webdev.app.jersey.resources.resp.ActReesponseRest;
import com.ibm.webdev.app.jpa.dao.Schedul;
import com.ibm.webdev.app.jpa.dao.Ticket;
import com.ibm.webdev.app.jpa.service.SchedulService;
import com.ibm.webdev.app.jpa.service.TicketService;
import com.ibm.webdev.app.websocket.client.MyWebSocketClient;

@Path("/tickets")
public class TicketResource {
    private static final String SUB_RESOURCE_PATH = "{ticket}";
    
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
            if(client.connectBlocking()) {
            	System.out.println("WebSocket connect successed !");
            }
        } catch (InterruptedException e) {
        	System.out.println("WebSocket connect faild !");
            e.printStackTrace();
        }
	}
	
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<ActReesponseRest> getTickets(@Context UriInfo uriInfo) throws Exception{
    	List<ActReesponseRest> restList = new ArrayList<ActReesponseRest>();
    	List<Ticket> datalist = service.findAllTickets();
		for(Ticket data : datalist) {
			TicketDto dto = new TicketDto();
			BeanUtils.copyProperties(data, dto);
			dto.setId(String.valueOf(data.getId()));
			dto.setSchedulId(String.valueOf(data.getSchedul().getId()));
			ActReesponseRest restObj = new ActReesponseRest();
        	URI location = uriInfo.getAbsolutePathBuilder().segment(String.valueOf(data.getId())).build();
        	restObj.setAction("getone");
        	restObj.setLink(location);
        	restObj.setContent(data);
        	restList.add(restObj);
		}
        return restList;
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest createTicket(@Valid TicketDto content,
                                   @Context UriInfo uriInfo) throws Exception  {
    	//不能为空
        if (content == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        // 是否存在
        Schedul insertSchedul = schedulService.findSchedulById(Long.valueOf(content.getSchedulId()));
        if (insertSchedul == null || insertSchedul.getId() == null) {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        // SYSDATE
        content.setBookingtime(new Timestamp(System.currentTimeMillis()));
        // 插入数据库
        Ticket insertdata = new Ticket();
        BeanUtils.copyProperties(content, insertdata);
        insertdata.setSchedul(insertSchedul);
        Long insertedId = service.insertTicketAndGetNewId(insertdata);
        if (insertedId == null || insertedId == 0L) throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);
        content.setId(String.valueOf(insertedId));
        URI location = uriInfo.getAbsolutePathBuilder().segment(content.getId()).build();

        // 公布新排行
        client.send(insertSchedul.getMovie().getName()+":"+insertSchedul.getPrice());
        
        ActReesponseRest obj = new ActReesponseRest();
        obj.setAction("inserted");
        obj.setContent(content);
        obj.setLink(location);
        return obj;
    }

    @Path(SUB_RESOURCE_PATH)
    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public TicketDto getTicket(@Context UriInfo uriInfo,
                               @PathParam("ticket") String ticketId) throws Exception  {
    	// 检查是否存在
    	Ticket ticket = service.findTicketById(Long.valueOf(ticketId));
        if (ticket == null || ticket.getId() == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        TicketDto dto = new TicketDto();
        BeanUtils.copyProperties(ticket, dto);
        dto.setId(ticketId);
        dto.setSchedulId(String.valueOf(ticket.getSchedul().getId()));
        return dto;
    }

    @Path(SUB_RESOURCE_PATH)
    @DELETE
    @Produces( MediaType.APPLICATION_JSON )
    public ActReesponseRest deleteTicket(@Context UriInfo uriInfo,
                                    @PathParam("ticket") String ticketId) throws Exception {
    	ActReesponseRest obj = new ActReesponseRest();
    	// 检查是否存在
        if (!service.existsById(Long.valueOf(ticketId))) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        // 删除数据库
        service.deleteTicketById(Long.valueOf(ticketId));
        
        URI location = uriInfo.getAbsolutePathBuilder().segment(ticketId).build();
        obj.setAction("deleted");
        obj.setLink(location);
        return obj;
    }
}
