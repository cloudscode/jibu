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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gaixie.jibu.security.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于登录成功后生成主窗口页面，一次性加载应用所有的javascript文件及CSS文件。
 * <p>
 * MainServlet 也响应主窗口的一些ajax请求，如菜单树加载。
 */
@ Singleton public class MainServlet extends HttpServlet {

    final static Logger logger = LoggerFactory.getLogger(MainServlet.class);

    @Inject private Injector injector;

    public String DOCTYPE =
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws IOException {
        mainPage(resp);
    }

    public void mainPage(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletOutputStream output=resp.getOutputStream();
        String title = getServletConfig().getInitParameter("main.title");
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        StringBuilder sb = new StringBuilder();
        sb.append(DOCTYPE + "\n" +
                  "<html>\n" +
                  "<head><title>" + title + "</title>\n" +
                  "  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\n" +
                  "  <link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"ext/resources/css/ext-all.css\" />\n" +
                  "  <script type=\"text/javascript\" src=\"ext/adapter/ext/ext-base.js\"></script>\n" +
                  "  <script type=\"text/javascript\" src=\"ext/ext-all.js\"></script>\n");

        String[] jsnames = getServletConfig().getInitParameter("js.names").split(",");
        for (String jsname:jsnames) {
            sb.append("  <script type=\"text/javascript\" src=\"js/"+jsname+"\"></script>\n");
        }
        sb.append("</head>\n" +
                  "<body></body>\n" +
                  "</html>\n");

        output.println(sb.toString());
        output.close();
    }

    public void doPost(HttpServletRequest req,  HttpServletResponse resp)
        throws IOException{
        doGet(req, resp);
    }

}

