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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;      
import java.io.PrintWriter;   

import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock; 
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.security.servlet.UserServlet;
import org.gaixie.jibu.security.servlet.NullHttpServletRequest;
import org.junit.Before;  
import org.junit.Test;  

public class UserServletTest {
    private UserServlet userServlet;  
    private NullHttpServletRequest nullRequest;  
    private HttpServletResponse mockResponse;  
    private ByteArrayOutputStream output;
    private UserService userService; 

    @Before public void setUp() {  
        userServlet = new UserServlet();  
        nullRequest = new NullHttpServletRequest();  

        //创建request和response的Mock  
        mockResponse = (HttpServletResponse) EasyMock.createMock(HttpServletResponse.class);  
        userService = EasyMock.createMock(UserService.class);  

    }  

    @Test public void testAdd() throws Exception {  
        nullRequest.setParameter("User.username", "tommy");
        nullRequest.setParameter("User.password", "123456");
        nullRequest.setParameter("User.fullname", "Tommy Wang");
        nullRequest.setParameter("User.emailaddress", "tommy@111.com");
        nullRequest.setParameter("User.enabled", "true");
        User user = new User("Tommy Wang","tommy","123456","tommy@111.com",true);
        //录制request和response的动作  
        output  = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
                public void write(final int theByte) {
                    output.write(theByte);
                }
            };
  
        //        PrintWriter pw = new PrintWriter(new OutputStreamWriter(output)); 
        EasyMock.expect(mockResponse.getOutputStream()).andReturn(sos);
        userService.add(null,user);
        EasyMock.expectLastCall().atLeastOnce();
        //回放  
        EasyMock.replay(mockResponse);
        EasyMock.replay(userService);

        userServlet.add(userService,nullRequest, mockResponse);
        System.out.println(output.toString());
        EasyMock.verify(mockResponse);
        EasyMock.verify(userService);
    }
}