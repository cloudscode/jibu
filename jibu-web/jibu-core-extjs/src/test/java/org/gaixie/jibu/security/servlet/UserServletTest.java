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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.easymock.EasyMock; 
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;
import org.gaixie.jibu.security.servlet.UserServlet;
import org.gaixie.jibu.security.servlet.NullHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;  
import org.junit.Test;  

public class UserServletTest {
    private UserServlet userServlet;
    private NullHttpServletRequest nullRequest;
    private Injector mockInjector;
    private UserService mockUserService;

    @Before public void setUp() {
        userServlet = new UserServlet();
        nullRequest = new NullHttpServletRequest();
        mockInjector = EasyMock.createMock(Injector.class);
        mockUserService = EasyMock.createMock(UserService.class);

    }

    @Test public void testUserAdd() throws Exception {
        nullRequest.setParameter("User.username", "tommy");
        nullRequest.setParameter("User.password", "123456");
        nullRequest.setParameter("User.fullname", "Tommy Wang");
        nullRequest.setParameter("User.emailaddress", "tommy@111.com");
        nullRequest.setParameter("User.enabled", "true");
        nullRequest.setLocale(new Locale("zh","CN"));
        User user = new User("Tommy Wang","tommy","123456","tommy@111.com",true);
        //录制request和response的动作
        EasyMock.expect(mockInjector.getInstance(UserService.class)).andReturn(mockUserService);
        mockUserService.add(user);
        EasyMock.expectLastCall().atLeastOnce();

        EasyMock.replay(mockInjector);
        EasyMock.replay(mockUserService);

        userServlet.userAdd(nullRequest,mockInjector);
        EasyMock.verify(mockUserService);
        EasyMock.verify(mockInjector);
    }
}