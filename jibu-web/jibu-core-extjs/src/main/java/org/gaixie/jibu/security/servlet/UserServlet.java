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
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaixie.jibu.json.JSONArray;
import org.gaixie.jibu.json.JSONException;
import org.gaixie.jibu.json.JSONObject;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@Singleton public class UserServlet extends HttpServlet {
    final static Logger logger = LoggerFactory.getLogger(UserServlet.class);

    @Inject private Injector injector;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws IOException {
        UserService userService = injector.getInstance(UserService.class);
        if ("add".equals(req.getParameter("ci"))) {
            add(userService,req,resp);
        }
        if ("find".equals(req.getParameter("ci"))) {
            find(userService,req,resp);
        }

    }

    public void doPost(HttpServletRequest req,  HttpServletResponse resp)
        throws IOException{
        doGet(req, resp);
    }

    public void add(UserService userService, 
                    HttpServletRequest req, 
                    HttpServletResponse resp) 
        throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ServletOutputStream output=resp.getOutputStream();
        try {
            User user = ServletUtils.httpToBean(User.class,req); 
            userService.add(null,user);
            jsonMap.put("success", true);
            jsonMap.put("data", user.getUsername());
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            output.println((new JSONObject(jsonMap)).toString());
            output.close();
        }
    }

    public void find(UserService userService, 
                     HttpServletRequest req, 
                     HttpServletResponse resp) 
        throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ServletOutputStream output=resp.getOutputStream();
        try {
            User user = ServletUtils.httpToBean(User.class,req); 
            List<User> users = userService.find(null,user);

            JSONArray array = new JSONArray();
            for (User u : users) {
                JSONObject obj = new JSONObject(u);
                array.put(obj);
            }

            jsonMap.put("success", true);
            jsonMap.put("data", array);
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            output.println((new JSONObject(jsonMap)).toString());
            output.close();
        }
    }
}
