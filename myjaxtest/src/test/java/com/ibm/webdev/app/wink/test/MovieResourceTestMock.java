/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/

package com.ibm.webdev.app.wink.test;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.apache.wink.server.internal.servlet.MockServletInvocationTest;
import org.apache.wink.test.mock.MockRequestConstructor;
import org.apache.wink.test.mock.TestUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ibm.webdev.app.jpa.service.MovieService;
import com.ibm.webdev.app.wink.resource.MovieResource;

/**
 *
 */
public class MovieResourceTestMock extends MockServletInvocationTest {

	//@Mock
    private MovieService service;
	
    @Override
    protected Class<?>[] getClasses() {
        return new Class[] {MovieResource.class};
    }

    @Override
    protected String getPropertiesFile() {
        return TestUtils.packageToPath(getClass().getPackage().getName()) + "/history.properties";
    }

    public void testGet() throws Exception {

        // check the collection
        MockHttpServletRequest request =
            MockRequestConstructor.constructMockRequest("GET",
                                                        "/movies",
                                                        MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = invoke(request);
        assertEquals("status", 200, response.getStatus());
        boolean diff = diffJSONArray("GetMovies-All.json", response.getContentAsString());
        assertFalse(diff);

        // get specific defect
        request =
            MockRequestConstructor.constructMockRequest("GET",
                                                        "/movies/1",
                                                        MediaType.APPLICATION_JSON);
        response = invoke(request);
        assertEquals("status", 200, response.getStatus());
        boolean diff2 = diffJSONObject("GetMovies-One.json", response.getContentAsString());
        assertFalse(diff2);
    }
    public void testPut() throws Exception {
      // try to update the defect, should return 404
    	MockHttpServletRequest request =
          MockRequestConstructor.constructMockRequest("PUT",
                                    "/movies/1",
                                    MediaType.APPLICATION_JSON,
                                    MediaType.APPLICATION_JSON,
                                    TestUtils
                                        .getResourceOfSamePackageAsBytes("PutMovies-One-Empty.json",
                                                                         getClass()));
    	MockHttpServletResponse response = invoke(request);
    	assertEquals("status", 404, response.getStatus());
    	System.out.println(response.getErrorMessage());
    	boolean diff3 = diffJSONObjectActor("movie-1-put.json", response.getContentAsString());
    	assertFalse(diff3);
    }
    
    private boolean diffJSONObject(String expectedFileName, String actual) throws Exception {
        InputStream expected = TestUtils.getResourceOfSamePackage(expectedFileName, getClass());
        JSONObject jsonfile = JSON.parseObject(expected);
        JSONObject response = JSON.parseObject(actual);
        return (jsonfile == response);
    }

    private boolean diffJSONObjectActor(String expectedFileName, String actual) throws Exception {
        InputStream expected = TestUtils.getResourceOfSamePackage(expectedFileName, getClass());
        JSONObject jsonfile = JSON.parseObject(expected);
        JSONObject response = JSON.parseObject(actual);
        return (jsonfile.get("actor") == response.getJSONObject("content").get("actor"));
    }
    
    private boolean diffJSONArray(String expectedFileName, String actual) throws Exception {
        InputStream expected = TestUtils.getResourceOfSamePackage(expectedFileName, getClass());
        JSONArray jsonfile = JSON.parseArray(expected);
        JSONArray response = JSON.parseArray(actual);
        return (jsonfile == response);
    }
}
