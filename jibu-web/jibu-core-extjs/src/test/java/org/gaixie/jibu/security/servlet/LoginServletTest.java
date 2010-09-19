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
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.gaixie.jibu.security.model.Setting;
import org.gaixie.jibu.security.service.LoginService;
import org.gaixie.jibu.security.service.SettingService;
import org.gaixie.jibu.security.servlet.LoginServlet;
import org.junit.Before;
import org.junit.Test;

public class LoginServletTest {
    private LoginServlet loginServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private ByteArrayOutputStream output;
    private LoginService mockLoginService;
    private SettingService mockSettingService;
    private Injector mockInjector;

    @Before public void setUp() {
        loginServlet = new LoginServlet();

        //创建request和response的Mock
        mockRequest = (HttpServletRequest)EasyMock.createMock(HttpServletRequest.class);
        mockResponse = (HttpServletResponse) EasyMock.createMock(HttpServletResponse.class);
        mockInjector = EasyMock.createMock(Injector.class);
        mockLoginService = EasyMock.createMock(LoginService.class);
        mockSettingService = EasyMock.createMock(SettingService.class);

    }

    @Test
        public void testLoginSuccess() throws Exception {
        //录制request和response的动作
        EasyMock.expect(mockRequest.getParameter("username")).andReturn("admin");
        EasyMock.expect(mockRequest.getParameter("password")).andReturn("123456");
        Locale locale = new Locale("zh","CN");
        EasyMock.expect(mockRequest.getLocale()).andReturn(locale);
        EasyMock.expect(mockInjector.getInstance(LoginService.class)).andReturn(mockLoginService);
        EasyMock.expect(mockInjector.getInstance(SettingService.class)).andReturn(mockSettingService);

        mockLoginService.login("admin","123456");
        EasyMock.expectLastCall().once();
        HttpSession ses = (HttpSession) EasyMock.createMock(HttpSession.class);
        ses.setAttribute("username", "admin");
        Setting setting = new Setting("language","zh_CN",0,true);
        EasyMock.expect(mockSettingService.getByUsername("language","admin")).andReturn(null);
        //EasyMock.expect(mockSettingService.getByUsername("language","admin")).andReturn(setting);
        ses.setAttribute("locale", locale);
        EasyMock.expect(mockRequest.getSession(true)).andReturn(ses);
        mockResponse.sendRedirect("MainServlet.y");
        //回放
        EasyMock.replay(mockRequest);
        EasyMock.replay(mockResponse);
        EasyMock.replay(mockInjector);
        EasyMock.replay(mockLoginService);
        EasyMock.replay(mockSettingService);
        EasyMock.replay(ses);

        loginServlet.login(mockInjector,mockRequest, mockResponse);
        EasyMock.verify(mockRequest);
        EasyMock.verify(mockResponse);
        EasyMock.verify(mockInjector);
        EasyMock.verify(mockLoginService);
        EasyMock.verify(mockSettingService);
        EasyMock.verify(ses);
    }
}