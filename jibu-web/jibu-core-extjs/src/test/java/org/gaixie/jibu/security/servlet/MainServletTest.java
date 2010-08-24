/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.gaixie.jibu.security.servlet;

import com.google.inject.Injector;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.gaixie.jibu.security.service.AuthorityService;
import org.gaixie.jibu.security.servlet.MainServlet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MainServletTest {
    private MainServlet mainServlet;
    private HttpServletRequest mockRequest;
    private AuthorityService mockAuthService;

    @Before public void setUp() {
        mainServlet = new MainServlet();
        mockRequest = (HttpServletRequest)EasyMock.createMock(HttpServletRequest.class);
        mockAuthService = EasyMock.createMock(AuthorityService.class);


    }

    @Test public void testLoadData() throws Exception {
        HttpSession mockSes = (HttpSession) EasyMock.createMock(HttpSession.class);
        EasyMock.expect(mockRequest.getSession(false)).andReturn(mockSes);
        EasyMock.expect((String)mockSes.getAttribute("username")).andReturn("admin");
        ResourceBundle rb = 
            ResourceBundle.getBundle("i18n/CoreExtjsResources");
        Map<String,String> map = new TreeMap<String,String>();
        map.put("system","#");
        map.put("system.administration","#");
        map.put("system.administration.pm","/PMServlet.z");
        map.put("system.preferences","/PreferServet.z");
        EasyMock.expect(mockAuthService.findMapByUsername("admin")).andReturn(map);

        EasyMock.replay(mockRequest);
        EasyMock.replay(mockSes);
        EasyMock.replay(mockAuthService);

        String data = mainServlet.loadData(mockAuthService,mockRequest);
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("JibuNav={};\n");
        sb.append("JibuNav.data = [{\"url\":\"system\",\"text\":\""
                  +rb.getString("system")+"\",\"leaf\":false,\"children\":[");
        sb.append("{\"url\":\"system.administration\",\"text\":\""
                  +rb.getString("system.administration")+"\",\"leaf\":false,\"children\":[");
        sb.append("{\"url\":\"system.administration.pm\",\"text\":\""
                  +rb.getString("system.administration.pm")+"\",\"leaf\":true}]},");
        sb.append("{\"url\":\"system.preferences\",\"text\":\""+rb.getString("system.preferences")+"\",\"leaf\":true}]}];\n");
        sb.append("</script>\n");
        sb.append("  <script type=\"text/javascript\" src=\"js/system/system-all.js\"></script>\n");
        sb.append("  <script type=\"text/javascript\" src=\"js/system/administration/administration-all.js\"></script>\n");

        Assert.assertTrue(data.equals(sb.toString()));
        EasyMock.verify(mockRequest);
        EasyMock.verify(mockSes);
        EasyMock.verify(mockAuthService);
    }
}