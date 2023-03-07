package com.ibm.webdev.app.jersey.start.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ibm.webdev.app.jersey.resources.resp.SimpleMsg;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException e) {
        List<String> strList = new ArrayList<String>();
        for (ConstraintViolation<?> cv : ((ConstraintViolationException) e).getConstraintViolations()) {
        	strList.add(cv.getMessage());
        }
        return Response
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .type(MediaType.APPLICATION_JSON)
                .entity(new SimpleMsg(strList))
                .build();
    }
}

