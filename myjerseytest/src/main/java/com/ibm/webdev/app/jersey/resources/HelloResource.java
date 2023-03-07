package com.ibm.webdev.app.jersey.resources;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.webdev.app.jersey.annotation.NonEmptyNames;
import com.ibm.webdev.app.jersey.resources.dto.HelloDto;

@Service
@Path("/hello")
public class HelloResource {
    @Autowired
    private Validator validator;
    
	@GET
    @Produces("text/plain")
    public String hello() {
        return "Hello from Spring";
    }
	
	@POST
    @Produces( MediaType.APPLICATION_JSON )
	@Consumes( MediaType.APPLICATION_JSON )
	@NonEmptyNames
    public String sayHello(@Valid HelloDto dto) throws BadRequestException {
        Set<ConstraintViolation<HelloDto>> sets = validator.validate(dto, Default.class);
        //合规检查
        if (!sets.isEmpty()) {
        	StringBuffer sb = new StringBuffer();
        	for (ConstraintViolation<HelloDto> cons : sets) {
        		sb.append(cons.getMessage());
        	}
           	//throw new WebApplicationException(new Throwable(sb.toString()),Response.Status.BAD_REQUEST);
        	throw new BadRequestException(sb.toString());
        }
        return "Hello from "+ dto.getName();
    }
}
