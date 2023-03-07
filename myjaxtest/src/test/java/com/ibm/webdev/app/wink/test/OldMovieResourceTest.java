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
import com.ibm.webdev.app.wink.resource.OldMovieResource;

/**
 *
 */
public class OldMovieResourceTest extends MockServletInvocationTest {

    @Override
    protected Class<?>[] getClasses() {
        return new Class[] {OldMovieResource.class};
    }

    @Override
    protected String getPropertiesFile() {
        return TestUtils.packageToPath(getClass().getPackage().getName()) + "/history.properties";
    }

    public void testGet() throws Exception {

        // Get All test case
        MockHttpServletRequest request =
            MockRequestConstructor.constructMockRequest("GET",
                                                        "/old/movies",
                                                        MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = invoke(request);
        assertEquals("status", 200, response.getStatus());
        response.setCharacterEncoding("utf-8");
        boolean diff = diffJSONArray("GetMovies-All.json", response.getContentAsString());
        assertTrue(diff);

        // Get One test case
        request =
            MockRequestConstructor.constructMockRequest("GET",
                                                        "/old/movies/1",
                                                        MediaType.APPLICATION_JSON);
        response = invoke(request);
        assertEquals("status", 200, response.getStatus());
        response.setCharacterEncoding("utf-8");
        boolean diff2 = diffJSONObject("GetMovies-One.json", response.getContentAsString());
        assertTrue(diff2);
    }
 
    
    private boolean diffJSONObject(String expectedFileName, String actual) throws Exception {
        InputStream expected = TestUtils.getResourceOfSamePackage(expectedFileName, getClass());
        JSONObject jsonfile = JSON.parseObject(expected);
        JSONObject response = JSON.parseObject(actual);
        return (jsonfile.getString("name").equals(response.getString("name"))) &&
         	   (jsonfile.getString("actor").equals(response.getString("actor"))) &&
         	   (jsonfile.getString("director").equals(response.getString("director")));
    }
    
    private boolean diffJSONArray(String expectedFileName, String actual) throws Exception {
        InputStream expected = TestUtils.getResourceOfSamePackage(expectedFileName, getClass());
        JSONArray jsonfile = JSON.parseArray(expected);
        JSONArray response = JSON.parseArray(actual);
        return (jsonfile.getJSONObject(0).getJSONObject("content").getString("name").equals(    response.getJSONObject(0).getJSONObject("content").getString("name"))) &&
        	   (jsonfile.getJSONObject(0).getJSONObject("content").getString("actor").equals(   response.getJSONObject(0).getJSONObject("content").getString("actor"))) &&
        	   (jsonfile.getJSONObject(0).getJSONObject("content").getString("director").equals(response.getJSONObject(0).getJSONObject("content").getString("director")));
 
    }
}
