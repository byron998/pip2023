package com.ibm.webdev.app.jersey.start.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.ibm.webdev.app.jersey.resources.CinemaResource;
import com.ibm.webdev.app.jersey.resources.HelloResource;
import com.ibm.webdev.app.jersey.resources.MovieResource;
import com.ibm.webdev.app.jersey.resources.SchedulResource;
import com.ibm.webdev.app.jersey.resources.TicketResource;
import com.ibm.webdev.app.jersey.start.mapper.ValidationExceptionMapper;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.AcceptHeaderApiListingResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;



@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(HelloResource.class);
        register(MovieResource.class);
        register(CinemaResource.class);
        register(SchedulResource.class);
        register(TicketResource.class);
        register(ValidationExceptionMapper.class);
        
        // swagger config
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        register(AcceptHeaderApiListingResource.class);
        
        BeanConfig config =  new  BeanConfig();
        config.setConfigId( "springboot-jersey-swagger" );
        config.setTitle( "Spring Boot + Jersey + Swagger" );
        config.setVersion( "1.0.0" );
        config.setContact( "cxb" );
        config.setSchemes( new  String[] {  "http" ,  "https"  });
        config.setBasePath("/rest");
        config.setResourcePackage( "com.ibm.webdev.app.jersey.resources" );
        config.setPrettyPrint( true );
        config.setScan( true );

    }

}


