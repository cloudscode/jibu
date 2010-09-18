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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gaixie.jibu.json.JSONException;
import org.gaixie.jibu.json.JSONObject;
import org.gaixie.jibu.security.model.Setting;
import org.gaixie.jibu.security.service.LoginException;
import org.gaixie.jibu.security.service.LoginService;
import org.gaixie.jibu.security.service.SettingService;
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


        if ("login".equals(req.getParameter("ci"))) {
            login(injector,req,resp);
        } else if ("logout".equals(req.getParameter("ci"))) {
            logout(req,resp);
        } else {
            loadPage(req,resp,null);
        }
    }

    public void doPost(HttpServletRequest req,  HttpServletResponse resp)
        throws IOException{
        doGet(req, resp);
    }

    protected void login(Injector injector,
                         HttpServletRequest req,
                         HttpServletResponse resp)
        throws IOException {
        LoginService loginService = injector.getInstance(LoginService.class);
        SettingService settingService = injector.getInstance(SettingService.class);
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        try {
            loginService.login(username, password);
            // check if we have a session
            HttpSession ses = req.getSession(true);
            ses.setAttribute("username", username);

            // 首先看用户是否选择过语言。
            // 如果选择过，直接读取选择的语言，以Locale对象放入session。
            // 如果没有选择，取当前浏览器支持的默认locale ，并放入session。
            Setting setting = settingService.getByUsername("language",username);
            if (setting != null) {
                ses.setAttribute("locale", ServletUtils.convertToLocale(setting.getValue()));
            } else {
                ses.setAttribute("locale", req.getLocale());
                // ses.setAttribute("locale", new Locale("zh","CN"));
            }
            resp.sendRedirect("MainServlet.y");
        } catch (LoginException le) {
            loadPage(req,resp,"login.message.001");
        }
    }

    protected void logout(HttpServletRequest req,
                          HttpServletResponse resp)
        throws IOException {
        // check if we have a session
        HttpSession ses = req.getSession(false);
        if (ses != null) {
            ses.invalidate();
        }
        resp.sendRedirect("/");
    }

    protected void loadPage(HttpServletRequest req,
                            HttpServletResponse resp,String message)
        throws IOException {
        HttpSession ses = req.getSession(false);
        if (ses != null) {
            String username = (String)ses.getAttribute("username");
            if (username != null) {
                resp.sendRedirect("MainServlet.y");
                return;
            }
        }

        resp.setContentType("text/html;charset=UTF-8");
        ResourceBundle rb =
            ResourceBundle.getBundle("i18n/CoreExtjsResources",
                                     ServletUtils.getLocale(req));

        StringBuilder sb = new StringBuilder();
        sb.append("<div id=\"login\">");
        if (message != null) {
            sb.append("  <div id=\"login_error\">");
            sb.append(rb.getString(message));
            sb.append("  </div>");
        }
        sb.append("  <form id=\"login_form\" method=\"post\" action=\"LoginServlet.x?ci=login\">\n"+
                  "    <p>\n"+
                  "      <label>"+rb.getString("login.username")+"<br>\n"+
                  "      <input id=\"user_name\" type=\"text\" value=\"\" name=\"username\"/>\n"+
                  "      </label>\n"+
                  "    </p>\n"+
                  "    <p>\n"+
                  "      <label>"+rb.getString("login.password")+"<br>\n"+
                  "      <input id=\"user_pass\" type=\"password\" value=\"\" name=\"password\"/>\n"+
                  "      </label>\n"+
                  "    </p>\n"+
                  "    <input id=\"login_button\" type=\"submit\" value=\""+rb.getString("button.login")+"\" />\n"+
                  "    <a id=\"lostpw\" href=\"#\">"+rb.getString("login.lostpassword")+"</a>\n"+
                  "  </form>\n");
        sb.append("</div>");

        PrintWriter pw = resp.getWriter();
        pw.println(ServletUtils.head("Jibu")+
                   loginCSS()+
                   ServletUtils.body()+
                   sb.toString()+
                   ServletUtils.footer());
        pw.close();
    }

    private String loginCSS() {
        StringBuilder sb = new StringBuilder();
        sb.append("<style type=\"text/css\">");

        sb.append("* {margin:0;padding:0;}\n");
        sb.append("#login {margin:7em auto;width:320px;}\n");
        sb.append("#login_error {background-color:#FFEBE8;border-color:#CC0000;"+
                  "border-style:solid;border-width:1px;margin:0 0 16px 8px;padding:10px 16px;font-size:11px;}\n");

        sb.append("#login_form {-moz-box-shadow:0 4px 18px #C8C8C8;background:#FFFFFF none repeat scroll 0 0;"+
                  "border:1px solid #CCCCCC;font-weight:normal;margin-left:8px;margin-bottom:0;padding:16px;}\n");
        sb.append("label {font-size:12px;}\n");
        sb.append("#user_pass, #user_name {border:1px solid #CCCCCC;font-size:18px;margin-bottom:12px;"+
                  "margin-right:6px;margin-top:2px;padding:3px;width:97%;}\n");
        sb.append("#login_button {font-size:12px;padding-left:12px;padding-right:12px;padding-top:2px;width:auto;}\n");
        sb.append("#lostpw {font-size:11px;}\n");

        sb.append("</style>");
        return sb.toString();
    }
}