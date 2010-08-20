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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gaixie.jibu.json.JSONException;
import org.gaixie.jibu.json.JSONObject;
import org.gaixie.jibu.security.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 响应登录请求的 Servlet，不被任何 Filter拦截。
 * <p> 
 */
@Singleton public class LoginServlet extends HttpServlet {
    final static Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Inject private Injector injector;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws IOException {
        LoginService loginService = injector.getInstance(LoginService.class);
        resp.setContentType("application/json;charset=UTF-8");
        if ("login".equals(req.getParameter("ci"))) {
            login(loginService,req,resp);
        }
    }

    public void doPost(HttpServletRequest req,  HttpServletResponse resp)
        throws IOException{
        doGet(req, resp);
    }

    public void login(LoginService loginService, 
                      HttpServletRequest req, 
                      HttpServletResponse resp) 
        throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        ServletOutputStream output=resp.getOutputStream();

        try {
            loginService.login(username, password);
            // check if we have a session
            HttpSession ses = req.getSession(true);
            ses.putValue("username", username);

            //resp.sendRedirect("/main.y");
            jsonMap.put("success", true);
            jsonMap.put("data", username);
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", "Login Failed! "+e.getMessage());
        } finally {
            output.println((new JSONObject(jsonMap)).toString());
            output.close();
        }
    }
}
