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

import java.beans.IntrospectionException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.utils.BeanConverter;
import org.gaixie.jibu.security.model.Criteria;

/**
 * Servlet 工具类。
 * <p>
 */
public class ServletUtils {

    public static final String DOCTYPE =
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";

    /**
     * 开始 html, head 标签。
     * <p>
     * @param title title 标签内容。
     * @return {@code <html><head><title>title</title><meta ..../>} 
     */
    public static String head(String title) {
        return(DOCTYPE + "\n" +
               "<html>\n" +
               "<head><title>" + title + "</title>\n" +
               "  <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\n");
    }

    /**
     * 加载 css 文件的标签。
     * <p>
     * @param path 文件路径。
     * @return {@code <link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"path\" />} 
     */
    public static String css(String path) {
        return("  <link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\""+path+"\" />\n");
    }

    /**
     * 加载 javascript 文件的标签。
     * <p>
     * @param path 文件路径。
     * @return {@code <script type=\"text/javascript\" src=\"path\"></script>} 
     */
    public static String javascript(String path) {
        return("  <script type=\"text/javascript\" src=\""+path+"\"></script>\n");
    }

    /**
     * 封闭 head，开始 body 标签。
     * <p>
     * @return {@code </head><body>} 
     */
    public static String body() {
        return("</head>\n"+
               "<body>");
    }

    /**
     * 封闭 body 和 html 标签。
     * <p>
     * @return {@code </body></html>} 
     */
    public static String footer() {
        return("</body>\n"+
               "</html>");
    }

    /**
     * 返回一个div 。
     * <p>
     * @param id div 的 id。
     * @param content div 的内容。
     * @return {@code <div id=\"name\">content</div>}
     */
    public static String div(String id,String content) {
        return("<div id=\""+id+"\">"+content+"</div>");
    }

    /**
     * 将 HttpServletRequest 中的值按照约定自动装入指定的 Javabean。
     * <p>
     * 如果 request 中的 name 满足 ClassName.property 的格式，会自动
     * 取出对应的 value 并装入Bean 相应属性 。如：User.password=123456，
     * 那么会将 123456 set 到 User 的 password 属性中。 <br>
     * @param cls 要生成的 Bean Class。
     * @param req 要处理的 Request。
     * @return 生成的 Javabean
     */
    public static <T> T httpToBean(Class<T> cls, HttpServletRequest req) 
        throws JibuException {
        HashMap map = new HashMap();
        Enumeration names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = req.getParameter(name);
            if (!value.trim().isEmpty()) {
                map.put(name, req.getParameter(name));
            }
        }
        T bean = BeanConverter.mapToBean(cls, map);
        return bean;
    }


    /**
     * 将 HttpServletRequest 中用于分页和排序的值按照约定自动装入 Criteria 对象。
     * <p>
     * 接收的参数有： limit , start , dir , sort。<br>
     * 如果 limit 的值大于 0 ，分页有效。如果 sort 非 null，排序有效。
     * @param req 要处理的 Request。
     * @return 如果没有有效的 limit 和 sort，返回 null，否则返回 Criteria 实例。
     */
    public static Criteria httpToCriteria(HttpServletRequest req) {
        Criteria criteria = new Criteria();

        if (null != req.getParameter("limit") &&
            null != req.getParameter("start")) {
            
            int limit = Integer.parseInt(req.getParameter("limit"));
            int start = Integer.parseInt(req.getParameter("start"));
            criteria.setLimit(limit);
            criteria.setStart(start);
        }

        criteria.setDir(req.getParameter("dir"));
        criteria.setSort(req.getParameter("sort"));

        if (criteria.getLimit() <=0 || criteria.getSort() == null) {
            return null;
        }
        return criteria;
    }

    /**
     * 从 Session 中得到当前用户的用户名 。
     * <p>
     * @param req HttpServletRequest。
     * @return username
     */
    public static String getUsername(HttpServletRequest req) {
        HttpSession ses = req.getSession(false);
        String username = null;
        if (null!=ses) {
            username = (String) ses.getValue("username"); 
        }
        return username;
    }

    /**
     * 从 Session 中得到当前用户的语言设置 。
     * <p>
     * @param req HttpServletRequest。
     * @return locale
     */
    public static String getLocale(HttpServletRequest req) {
        HttpSession ses = req.getSession(false);
        String locale = null;
        if (null!=ses) {
            locale = (String) ses.getValue("locale"); 
        }
        return locale;
    }
}