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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.json.JSONArray;
import org.gaixie.jibu.json.JSONException;
import org.gaixie.jibu.json.JSONObject;
import org.gaixie.jibu.security.annotation.CIVerify;
import org.gaixie.jibu.security.model.Criteria;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.UserService;

/**
 * 响应用户管理相关操作的请求。
 * <p>
 */
@Singleton public class UserServlet extends HttpServlet {
    @Inject private Injector injector;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String rst = null;
        if ("userAdd".equals(req.getParameter("ci"))) {
            rst = userAdd(req,injector);
        } else if ("userUpdate".equals(req.getParameter("ci"))) {
            rst = userUpdate(req,injector);
        } else if ("userDelete".equals(req.getParameter("ci"))) {
            rst = userDelete(req,injector);
        } else if ("userFind".equals(req.getParameter("ci"))) {
            rst = userFind(req,injector);
        }

        PrintWriter pw = resp.getWriter();
        pw.println(rst);
        pw.close();
    }

    public void doPost(HttpServletRequest req,  HttpServletResponse resp)
        throws IOException{
        doGet(req, resp);
    }

    @CIVerify
        protected String userAdd(HttpServletRequest req,
                                 Injector injector) {
        UserService userService = injector.getInstance(UserService.class);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            User user = ServletUtils.httpToBean(User.class,req);
            userService.add(user);
            jsonMap.put("success", true);
            jsonMap.put("message", "操作成功！");
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            return (new JSONObject(jsonMap)).toString();
        }
    }

    protected String userUpdate(HttpServletRequest req,
                                Injector injector) {
        UserService userService = injector.getInstance(UserService.class);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            User user = ServletUtils.httpToBean(User.class,req);
            userService.update(user);
            jsonMap.put("success", true);
            jsonMap.put("message", "更新成功!");
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            return (new JSONObject(jsonMap)).toString();
        }
    }

    @CIVerify
        protected String userDelete(HttpServletRequest req,
                                    Injector injector) {
        UserService userService = injector.getInstance(UserService.class);

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String id = req.getParameter("id");
        try {
            User user = new User();
            user.setId(Integer.parseInt(id));
            userService.delete(user);
            jsonMap.put("success", true);
            jsonMap.put("message", "成功删除");
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            return (new JSONObject(jsonMap)).toString();
        }
    }

    @CIVerify
        protected String userFind(HttpServletRequest req,
                                  Injector injector) {
        UserService userService = injector.getInstance(UserService.class);
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        try {
            User user = ServletUtils.httpToBean(User.class,req);
            Criteria criteria = ServletUtils.httpToCriteria(req);
            List<User> users = userService.find(user,criteria);
            jsonMap.put("success", true);
            jsonMap.put("data", new JSONArray(users,false));
            if (null != criteria && criteria.getLimit()>0) {
                jsonMap.put("total", criteria.getTotal());
            }
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            return (new JSONObject(jsonMap)).toString();
        }
    }
}
