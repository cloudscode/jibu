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
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.service.AuthorityService;

/**
 * 响应权限资源相关操作的请求。
 * <p>
 */
@Singleton public class AuthorityServlet extends HttpServlet {
    @Inject private Injector injector;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        String rst = null;
        if ("authFind".equals(req.getParameter("ci"))) {
            rst = authFind(req,injector);
        } else if ("authAdd".equals(req.getParameter("ci"))) {
            rst = authAdd(req,injector);
        } else if ("authUpdate".equals(req.getParameter("ci"))) {
            rst = authUpdate(req,injector);
        } else if ("authDelete".equals(req.getParameter("ci"))) {
            rst = authDelete(req,injector);
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
        protected String authFind(HttpServletRequest req,
                                  Injector injector) {
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ResourceBundle rb =
            ResourceBundle.getBundle("i18n/CoreExtjsResources");
        try {
            Authority auth = ServletUtils.httpToBean(Authority.class,req);
            List<Authority> auths = authService.find(auth);
            for (Authority a : auths) {
                if (a.getName().length() >=5 ) {
                    a.setName(rb.getString(a.getName()));
                }
            }
            jsonMap.put("success", true);
            jsonMap.put("data", new JSONArray(auths,false));
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            return (new JSONObject(jsonMap)).toString();
        }
    }

    @CIVerify
        protected String authAdd(HttpServletRequest req,
                                 Injector injector) {
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            Authority auth = ServletUtils.httpToBean(Authority.class,req);
            authService.add(auth);
            jsonMap.put("success", true);
            jsonMap.put("message", "添加成功!");
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            return (new JSONObject(jsonMap)).toString();
        }
    }

    protected String authUpdate(HttpServletRequest req,
                                Injector injector) {
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            Authority auth = ServletUtils.httpToBean(Authority.class,req);
            authService.update(auth);
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
        protected String authDelete(HttpServletRequest req,
                                    Injector injector) {
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String id = req.getParameter("id");
        try {
            Authority auth = new Authority();
            auth.setId(Integer.parseInt(id));
            authService.delete(auth);
            jsonMap.put("success", true);
            jsonMap.put("message", "成功删除");
        } catch (Exception e) {
            jsonMap.put("success", false);
            jsonMap.put("message", e.getMessage());
        } finally {
            return (new JSONObject(jsonMap)).toString();
        }
    }
}
