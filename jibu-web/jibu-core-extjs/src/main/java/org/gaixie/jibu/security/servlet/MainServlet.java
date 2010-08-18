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

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        if ("loadMenu".equals(req.getParameter("ci"))) {
            AuthorityService authService = 
                injector.getInstance(AuthorityService.class);
            loadMenu(authService,req,resp);
        } else {
            mainPage(resp);
        }
    }

    public void loadMenu(AuthorityService authService, 
                         HttpServletRequest req, 
                         HttpServletResponse resp) 
        throws IOException {
        ServletOutputStream output=resp.getOutputStream();
        StringBuilder sb = new StringBuilder();
        try {
            HttpSession ses = req.getSession(false);
            Map<String,String> map = 
                authService.findMapByUsername((String)ses.getValue("username"));
            /*
            map.put("security","#");
            map.put("security.role","#");
            map.put("security.role.rolelist","a.z");
            map.put("security.role.roleadd","b.z");
            map.put("security.settings","c.z");
            map.put("security.user","#");
            map.put("security.user.userlist","d.z");


            for (int m=0 ;m<10;m++) {
                map.put("subsystem"+m,"#");
                for (int n=0;n<5;n++) {
                    map.put("subsystem"+m+"."+"sub"+n,"#");
                    for (int l=0 ;l<10;l++){
                        map.put("subsystem"+m+"."+"sub"+n+".gg","#");
                        map.put("subsystem"+m+"."+"sub"+n+".gg."+l,"ggg.z");
                    }
                }
            }
            */
            /* pre:  前一个节点的层数
             * cur:  当前节点的层数
             * dist: 上一个节点和当前节点的层距
             * index: 用于判断是否是第一个节点
             */
            int pre =0;
            int index =0;
            Iterator iter = map.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String)entry.getKey();
                String val = (String)entry.getValue();

                String node = "{\"url\":\""+val+"\",\"text\":\""+key+"\",\"leaf\":"
                    +(("#".equals(val)) ? "false" : "true}");

                int cur = key.split("\\.").length;
                int dist = pre - cur;
                
                if (dist < 0) {                      // 层数在增加
                    // 第一个节点按照JSON格式需要特殊处理
                    if (index == 0) sb.append("["+node);
                    else sb.append(",\"children\":["+node);
                } else if (dist >0) {                // 层数在减少
                    for (int i=0;i<dist;i++) {
                        sb.append("]}");
                    }
                    sb.append(","+node);
                } else {                             // 层数没变化
                    sb.append(","+node);
                }
                pre = cur;
                index++;
            }
            for (int i=0;i<pre-1;i++) {
                sb.append("]}");
            }
            if (map.size()>0) sb.append("]");
        } catch (Exception e) {
        } finally {
            output.println(sb.toString());
            output.close();
        }
    }

    public void mainPage(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletOutputStream output=resp.getOutputStream();
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        StringBuilder sb = new StringBuilder();
        sb.append(DOCTYPE + "\n" +
                  "<html>\n" +
                  "<head><title>Jibu Web Application</title>\n" +
                  "  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\n" +
                  "  <link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"ext/resources/css/ext-all.css\" />\n" +
                  "  <script type=\"text/javascript\" src=\"ext/adapter/ext/ext-base.js\"></script>\n" +
                  "  <script type=\"text/javascript\" src=\"ext/ext-all.js\"></script>\n");
        sb.append("  <link rel=\"stylesheet\" type=\"text/css\" href=\"js/classic/layout.css\">\n");
        sb.append("  <script type=\"text/javascript\" src=\"js/classic/layout.js\"></script>\n");
        sb.append("</head>\n" +
                  "<body><div id=\"header\"></div></body>\n" +
                  "</html>\n");

        output.println(sb.toString());
        output.close();
    }

    public void doPost(HttpServletRequest req,  HttpServletResponse resp)
        throws IOException{
        doGet(req, resp);
    }

}

