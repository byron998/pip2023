package com.ibm.webdev.app.jersey.start.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ibm.webdev.app.jersey.resources.resp.SimpleMsg;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
    	List<String> strList = new ArrayList<String>();
    	strList.add(e.getMessage());
        return Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .type(MediaType.APPLICATION_JSON)
                .entity(new SimpleMsg(strList))
                .build();
    }
}
