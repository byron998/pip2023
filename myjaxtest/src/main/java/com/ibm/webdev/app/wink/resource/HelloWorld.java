package com.ibm.webdev.app.wink.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;


@Path("/hello")
//@OpenApi(value = "my helloworld", description = "My JAXRS Service API")
public class HelloWorld {
  // This method is called if HTML and XML is not requested  
  @GET
  @Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
  //@ApiOperation(value = "say hello", response = JSONObject.class)
  public JSONObject sayPlainTextHello() throws JSONException {
	JSONObject retobj =  new JSONObject();
	retobj.put("hello", "我的名字");
    return retobj;  
  }  
//  // This method is called if XML is requested  
//  @GET
//  @Produces(MediaType.TEXT_XML)
//  public String sayXMLHello() {  
//    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";  
//  }  
//
//  // This method is called if HTML is requested  
//  @GET
//  @Produces(MediaType.TEXT_HTML)  
//  public String sayHtmlHello() {  
//    return "<html> " + "<title>" + "Hello Jersey" + "</title>"  
//    + "<body><h1>" + "Hello Jersey HTML" + "</h1></body>" + "</html> ";  
//  }
}
