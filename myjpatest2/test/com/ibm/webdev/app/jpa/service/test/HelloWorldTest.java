package com.ibm.webdev.app.jpa.service.test;
//package com.ibm.webdev.test.wink.resource;
//
//import java.io.FileNotFoundException;
//import java.io.StringReader;
//import java.util.Set;
//
//import javax.ws.rs.core.MediaType;
//
//import org.apache.wink.common.http.HttpStatus;
//import org.apache.wink.common.internal.application.ApplicationFileLoader;
//import org.apache.wink.common.internal.utils.MediaTypeUtils;
//import org.apache.wink.common.model.atom.AtomEntry;
//import org.apache.wink.server.internal.servlet.MockServletInvocationTest;
//import org.json.JSONObject;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//
//import com.alibaba.fastjson2.JSON;
//
//public class HelloWorldTest extends MockServletInvocationTest {
//
//    @Override
//    protected Class<?>[] getClasses() {
//        try {
//            Set<Class<?>> classes = new ApplicationFileLoader("application").getClasses();
//            Class<?>[] classesArray = new Class[classes.size()];
//            return classes.toArray(classesArray);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void testHelloWorld() throws Exception {
//        MockHttpServletRequest request =
//            constructMockRequest("GET", "/hello", MediaTypeUtils.JSON_TYPES);
//        MockHttpServletResponse response = invoke(request);
//
//        assertEquals("HTTP status", HttpStatus.OK.getCode(), response.getStatus());
//        
//        String charset =
//            MediaType.valueOf(response.getContentType()).getParameters().get("charset");
//        response.setCharacterEncoding(charset);
//
//        JSONObject entry = new JSONObject(response.getContentAsString());
//        String name = entry.getString("hello");
//        assertEquals("你的名字", "你的名字", name);
//    }
//
//    private MockHttpServletRequest constructMockRequest(String method,
//                                                        String requestURI,
//                                                        String acceptHeader) {
//        MockHttpServletRequest mockRequest = new MockHttpServletRequest() {
//
//            public String getPathTranslated() {
//                return null; 
//            }
//
//        };
//        mockRequest.setMethod(method);
//        mockRequest.setRequestURI(requestURI);
//        mockRequest.addHeader("Accept", acceptHeader);
//        return mockRequest;
//    }
//}
