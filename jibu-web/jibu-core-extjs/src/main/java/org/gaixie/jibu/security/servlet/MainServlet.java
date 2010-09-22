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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gaixie.jibu.security.model.Setting;
import org.gaixie.jibu.security.service.AuthorityService;
import org.gaixie.jibu.security.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于登录成功后生成主窗口页面，加载应用共用的javascript文件及CSS文件。
 * <p>
 * MainServlet 也响应主窗口的一些ajax请求，如菜单树加载。
 */
@ Singleton public class MainServlet extends HttpServlet {
    final static Logger logger = LoggerFactory.getLogger(MainServlet.class);
    @Inject private Injector injector;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        AuthorityService authService = injector.getInstance(AuthorityService.class);
        SettingService settingService = injector.getInstance(SettingService.class);
        HttpSession ses = req.getSession(false);
        String username = (String)ses.getAttribute("username");

        List<Setting> settings = settingService.findByUsername(username);

        String theme = null;
        // 首先看用户是否选择过语言。
        // 如果选择过，直接读取选择的语言，以Locale对象放入session。
        // 如果没有选择，取当前浏览器支持的默认locale
        for (Setting setting :settings) {
            if ("theme".equals(setting.getName())) {
                theme = setting.getValue();
            } else if ("language".equals(setting.getName())) {
                ses.setAttribute("locale", ServletUtils.convertToLocale(setting.getValue()));
            }
        }
        Locale locale = ServletUtils.getLocale(req);

        PrintWriter pw = resp.getWriter();
        pw.println(ServletUtils.head("Jibu")+
                   ServletUtils.css("ext/resources/css/ext-all.css"));

        if (theme != null) {
            pw.println(ServletUtils.css("ext/resources/css/xtheme-"+
                                        theme.toLowerCase()+".css"));
        }

        pw.println(ServletUtils.javascript("ext/adapter/ext/ext-base.js")+
                   ServletUtils.javascript("ext/ext-all.js")+
                   ServletUtils.javascript("ext/locale/ext-lang-"+ getLanguage(locale)+".js")+
                   ServletUtils.css("css/jibu-all.css")+
                   ServletUtils.css("js/classic/layout.css")+
                   ServletUtils.javascript("ext-ux/ext-ux-all.js")+
                   loadData(authService,req)+
                   ServletUtils.javascript("js/classic/layout.js")+
                   ServletUtils.javascript("locale/js/classic/layout-"+ getLanguage(locale)+".js")+
                   ServletUtils.body()+
                   ServletUtils.div("header","")+
                   ServletUtils.footer());
        pw.close();
    }

    public String loadData(AuthorityService authService,
                           HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        // 用一个有序的 TreeSet，使其可以被Mock测试，性能损失可以被忽略。
        Set mod = new TreeSet();
        Locale locale = ServletUtils.getLocale(req);
        ResourceBundle rb =
            ResourceBundle.getBundle("i18n/CoreExtjsResources",locale);

        HttpSession ses = req.getSession(false);
        Map<String,String> map =
            authService.findMapByUsername((String)ses.getAttribute("username"));

        /*------------------------------------------------------------------------------------
         * 算法：
         * 提高初次载入的效率，需要用一遍循环得到
         * 1）用于菜单加载的 树形数据 （json格式）。
         *    放入<script> 标签内，直接返回，无须第二次 ajax请求。
         *    pre:  前一个节点的层数
         *    cur:  当前节点的层数
         *    dist: 上一个节点和当前节点的层距
         *    index: 用于判断是否是第一个节点
         * 2) 根据权限加载所需的 js文件。（确切的说应该是根据权限加载子系统 js文件)。
         *    如果权限 key = system.administration.pm 。
         *    根据约定，一定有一个 administraion-all.js 文件在 /js/system/administration 目录下。
         *    应该只加载此文件。同级目录下 js文件的压缩打包在各系统的 pom 中配置。
         *------------------------------------------------------------------------------------*/
        int pre =0;
        int index =0;
        Iterator iter = map.entrySet().iterator();
        // 定义一个全局变量，用来保存 json数据。
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("JibuNav={};\n");
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String)entry.getKey();
            String val = (String)entry.getValue();

            // text: key 应该是国际化过的串，用于显示，以menu为前缀。
            String node = "{url:\""+key+"\",text:\""+rb.getString(key)+"\",leaf:"
                +(("#".equals(val)) ? "false" : "true}");

            String[] path = key.split("\\.");
            int cur = path.length;

            if (!"#".equals(val)) {
                // s = 要加载的js 文件路经
                String s = key.substring(0,key.lastIndexOf("."));
                s = s.replace(".","/");
                mod.add("js/"+s+"/"+path[cur-2]);
            }

            int dist = pre - cur;

            if (dist < 0) {                      // 层数在增加
                // 第一个节点按照JSON格式需要特殊处理，注意在这里开始给 JibuNavs赋值
                if (index == 0) sb.append("JibuNav.data = ["+node);
                else sb.append(",children:["+node);
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
        if (map.size()>0) sb.append("];\n");
        sb.append("</script>\n");

        // 输出拥有权限的子系统 js 文件。
        Iterator ite = mod.iterator();
        String module;
        while (ite.hasNext()){
            module = (String)ite.next();
            sb.append("  <script type=\"text/javascript\" src=\""+module+"-all.js\"></script>\n");
            sb.append("  <script type=\"text/javascript\" src=\"locale/"+module+"-all-"+getLanguage(locale)+".js\"></script>\n");
        }

        /*
          sb.append("  <script type=\"text/javascript\" src=\"ext-ux/msg-bus.js\"></script>\n");
          sb.append("  <script type=\"text/javascript\" src=\"js/system/administration/pm.js\"></script>\n");
          sb.append("  <script type=\"text/javascript\" src=\"js/system/administration/user.js\"></script>\n");
          sb.append("  <script type=\"text/javascript\" src=\"js/system/administration/role.js\"></script>\n");
          sb.append("  <script type=\"text/javascript\" src=\"js/system/administration/authority.js\"></script>\n");
          sb.append("  <script type=\"text/javascript\" src=\"js/system/setting.js\"></script>\n");
        */
        return sb.toString();
    }

    private String getLanguage(Locale locale) {
        if (locale.getLanguage().equals(new Locale("zh", "", "").getLanguage())) {
            return locale.toString();
        }
        return locale.getLanguage();
    }

    public void doPost(HttpServletRequest req,  HttpServletResponse resp)
        throws IOException{
        doGet(req, resp);
    }
}
